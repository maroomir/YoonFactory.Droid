package com.yoonfactory.comm;

import com.yoonfactory.eYoonStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class YoonServer implements IYoonTcpIp, IReceiveMessageEventListener, IRetryOpenEventListener {
    private boolean m_bRetryOpen = false;
    private boolean m_bSend = false;
    private StringBuilder m_sbReceiveMessage;
    private String m_strAddress = "";
    private int m_nPort = 0;
    private int m_nCountClients = 10;
    private int m_nCountRetry = 10;
    private int m_nTimeout = 10000;
    private ServerSocket m_serverSocket = null;
    private Socket m_connectedClientSocket = null;

    public YoonServer() {
        // Initialize Manageable Variable
        m_sbReceiveMessage = new StringBuilder();
        // Event Subscription
        CommEventHandler.addRetryOpenListener(this);
        CommEventHandler.addReceiveMessageListener(this);
    }

    @Override
    public void onRetryOpenEvent() {
        listen();
    }

    @Override
    public void onReceiveMessageEvent(String strReceiveMessage) {
        m_sbReceiveMessage.append(strReceiveMessage);
        m_sbReceiveMessage.append(System.lineSeparator());
    }

    @Override
    public boolean isRetryOpen() {
        return m_bRetryOpen;
    }

    @Override
    public boolean isSend() {
        return m_bSend;
    }

    @Override
    public String getAddress() {
        return m_strAddress;
    }

    @Override
    public void setAddress(String m_strAddress) {
        this.m_strAddress = m_strAddress;
    }

    @Override
    public int getPort() {
        return m_nPort;
    }

    @Override
    public void setPort(int nPort) {
        this.m_nPort = nPort;
    }

    @Override
    public int getRetryCount() {
        return m_nCountRetry;
    }

    @Override
    public int getTimeout() {
        return m_nTimeout;
    }

    public int getConnectedCount() {
        return m_nCountClients;
    }

    public void setConnectedCount(int nCount) {
        m_nCountClients = nCount;
    }

    public boolean isBound() {
        if (m_serverSocket == null) return false;
        return m_serverSocket.isBound();
    }

    @Override
    public boolean isConnected() {
        if (m_connectedClientSocket == null) return false;
        return m_connectedClientSocket.isConnected();
    }

    @Override
    public StringBuilder getReceiveMessage() {
        return m_sbReceiveMessage;
    }

    @Override
    public void copyFrom(IYoonComm pComm) {
        if (pComm instanceof YoonServer) {
            YoonServer pServer = (YoonServer) pComm;
            close();
            if (pServer.isConnected())
                pServer.close();
            m_nPort = pServer.getPort();
            m_nCountRetry = pServer.getRetryCount();
            m_nTimeout = pServer.getTimeout();
        }
    }

    @Override
    public boolean open() {
        return listen();
    }

    public boolean listen(int nPort) {
        m_nPort = nPort;
        return listen();
    }

    private Thread m_threadAccept = null;

    public boolean listen() {
        if (m_connectedClientSocket != null && m_connectedClientSocket.isConnected())
            return true;
        try {
            if (m_serverSocket == null || !m_serverSocket.isBound()) {
                //// Binding port and Listening per backlogging
                m_serverSocket = new ServerSocket();
                if (!isRetryOpen())
                    CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, String.format("Listen Port : %d", m_nPort), false);
                int m_nBacklog = 5;
                m_serverSocket.bind(new InetSocketAddress(m_nPort), m_nBacklog);
                int BUFFER_SIZE = 4096;
                m_serverSocket.setReceiveBufferSize(BUFFER_SIZE);
                //// Start the client accept thread
                if (m_threadAccept == null) {
                    m_threadAccept = new Thread(() -> {
                        int nCount = 0;
                        CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, "Start Accept Thread");
                        try {
                            while (nCount < m_nCountClients) {
                                if (acceptClient())
                                    nCount++;
                                Thread.sleep(10);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Conform, "Exit Thread : " + Integer.toString(nCount));
                        }
                    });
                    m_threadAccept.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, e.getMessage());
            m_bRetryOpen = false;
            if (m_serverSocket != null)
                m_serverSocket = null;
            return false;
        }
        if (m_serverSocket.isBound()) {
            CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, "Listen Success");
            m_bRetryOpen = false;
            return false;
        } else {
            m_bRetryOpen = true;
            return true;
        }
    }

    private Thread m_threadSocket = null;
    private ActiveTcpRunnable m_pRunnableSocket = null;

    private boolean acceptClient() {
        try {
            m_connectedClientSocket = m_serverSocket.accept();
            if (m_connectedClientSocket == null) {
                CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, "Client Connection Failure");
            } else {
                CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, "Accept Client : " + m_connectedClientSocket.getInetAddress().toString());
                m_pRunnableSocket = new ActiveTcpRunnable(m_connectedClientSocket);
                m_threadSocket = new Thread(m_pRunnableSocket);
                m_threadSocket.start();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        if (m_bRetryOpen)
            m_bRetryOpen = false;
        CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Info, "Close Listen");
        if (m_serverSocket == null)
            return;
        try {
            //// Stop Open Retry Thread
            OnStopRetryThread();
            //// Stop Active Tcp Thread
            if (m_pRunnableSocket != null && m_threadSocket != null) {
                m_pRunnableSocket.close();
                if (m_threadSocket.isAlive())
                    m_threadSocket.interrupt();
                m_pRunnableSocket = null;
                m_threadAccept = null;
            }
            //// Stop Active Accept Thread
            if (m_threadAccept != null) {
                if (m_threadAccept.isAlive())
                    m_threadAccept.interrupt();
                m_threadAccept = null;
            }
            //// Close the Socket
            m_serverSocket.close();
            //// Remove the Event Listener
            CommEventHandler.removeReceiveMessageListener(this);
            CommEventHandler.removeRetryOpenListener(this);
        } catch (IOException e) {
            e.printStackTrace();
            CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, e.getMessage());
        }
        m_serverSocket = null;
    }

    private Thread m_threadRetryListen = null;

    @Override
    public void OnStartRetryThread() {
        if (!m_bRetryOpen) return;
        if (m_threadRetryListen != null && m_threadRetryListen.isAlive()) return;
        m_threadRetryListen = new Thread(new RetryServerRunnable(m_serverSocket, m_nCountRetry, m_nTimeout));
        m_threadRetryListen.setName("Retry Listen");
        m_threadRetryListen.start();
    }

    @Override
    public void OnStopRetryThread() {
        if (m_threadRetryListen == null) return;
        if (m_threadRetryListen.isAlive())
            m_threadRetryListen.interrupt();
        m_threadRetryListen = null;
    }

    @Override
    public boolean send(String strBuffer) {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (!m_connectedClientSocket.isConnected()) {
            CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Connection Fail");
            return false;
        }
        try {
            m_bSend = m_pRunnableSocket.send(strBuffer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m_bSend;
    }

    @Override
    public boolean send(byte[] pBuffer) {
        if (m_serverSocket == null || m_connectedClientSocket == null)
            return false;
        if (!m_connectedClientSocket.isConnected()) {
            CommEventHandler.callShowMessageEvent(YoonServer.class, eYoonStatus.Error, "Send Failure : Connection Fail");
            return false;
        }
        try {
            String strBuffer = Arrays.toString(pBuffer);
            m_bSend = m_pRunnableSocket.send(strBuffer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m_bSend;
    }
}