package com.yoonfactory.comm;

import com.yoonfactory.eYoonStatus;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class YoonClient implements IYoonTcpIp, IReceiveMessageEventListener, IRetryOpenEventListener {
    private boolean m_bRetryOpen = false;
    private boolean m_bSend = false;
    private final StringBuilder m_sbReceiveMessage;
    private String m_strAddress = "";
    private int m_nPort = 0;
    private int m_nCountRetry = 10;
    private int m_nTimeout = 10000;
    private Socket m_clientSocket = null;

    public YoonClient() {
        // Initialize Manageable Variable
        m_sbReceiveMessage = new StringBuilder();
        // Event Subscription
        CommEventHandler.addRetryOpenListener(this);
        CommEventHandler.addReceiveMessageListener(this);
    }

    @Override
    public void onRetryOpenEvent() {
        connect();
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

    @Override
    public boolean isConnected() {
        if (m_clientSocket == null) return false;
        return m_clientSocket.isConnected();
    }

    @Override
    public StringBuilder getReceiveMessage() {
        return m_sbReceiveMessage;
    }

    @Override
    public void copyFrom(IYoonComm pComm) {
        if (pComm instanceof YoonClient) {
            YoonClient pClient = (YoonClient) pComm;
            close();
            if (pClient.isConnected())
                pClient.close();
            m_strAddress = pClient.getAddress();
            m_nPort = pClient.getPort();
            m_nCountRetry = pClient.getRetryCount();
            m_nTimeout = pClient.getTimeout();
        }
    }

    @Override
    public boolean open() {
        return connect();
    }

    public boolean connect(String strAddress, int nPort) {
        m_strAddress = strAddress;
        m_nPort = nPort;
        return connect();
    }

    private Thread m_threadSocket = null;
    private ActiveTcpRunnable m_pRunnableSocket = null;

    public boolean connect() {
        if (m_clientSocket != null && m_clientSocket.isConnected())
            return true;
        try {
            m_clientSocket = new Socket();
            if (!isRetryOpen())
                CommEventHandler.callShowMessageEvent(YoonClient.class, eYoonStatus.Info, String.format("Connection Attempt : %s / %d", m_strAddress, m_nPort));
            //// Connected port
            m_clientSocket.connect(new InetSocketAddress(m_strAddress, m_nPort), m_nTimeout);
            int BUFFER_SIZE = 4096;
            m_clientSocket.setReceiveBufferSize(BUFFER_SIZE);
            //// Run Socket Thread
            m_pRunnableSocket = new ActiveTcpRunnable(m_clientSocket);
            m_threadSocket = new Thread(m_pRunnableSocket);
            m_threadSocket.start();
        } catch (IOException e) {
            e.printStackTrace();
            CommEventHandler.callShowMessageEvent(YoonClient.class, eYoonStatus.Error, e.getMessage());
            m_bRetryOpen = false;
            if (m_clientSocket != null)
                m_clientSocket = null;
            return false;
        }
        if (m_clientSocket.isConnected()) {
            CommEventHandler.callShowMessageEvent(YoonClient.class, eYoonStatus.Info, "Connection Success");
            m_bRetryOpen = false;
        } else {
            if (!m_bRetryOpen)
                CommEventHandler.callShowMessageEvent(YoonClient.class, eYoonStatus.Info, "Connection Failure");
            m_bRetryOpen = true;
        }
        return m_clientSocket.isConnected();
    }

    public void close() {
        if (m_bRetryOpen)
            m_bRetryOpen = false;
        CommEventHandler.callShowMessageEvent(YoonClient.class, eYoonStatus.Info, "Close Connection");
        if (m_clientSocket == null)
            return;
        try {
            //// Stop Open Retry Thread
            OnStopRetryThread();
            //// Stop Active Tcp Thread
            if(m_pRunnableSocket != null)
            {
                m_pRunnableSocket.close();
                if (m_threadSocket.isAlive())
                    m_threadSocket.interrupt();
            }
            //// Close the Socket
            m_clientSocket.close();
            //// Remove the Event Listener
            CommEventHandler.removeReceiveMessageListener(this);
            CommEventHandler.removeRetryOpenListener(this);

        } catch (IOException e) {
            e.printStackTrace();
            CommEventHandler.callShowMessageEvent(YoonClient.class, eYoonStatus.Error, e.getMessage());
        }
        m_clientSocket = null;
    }

    private Thread m_threadRetryConnection = null;

    @Override
    public void OnStartRetryThread() {
        if (!m_bRetryOpen) return;
        if (m_threadRetryConnection != null && m_threadRetryConnection.isAlive()) return;
        m_threadRetryConnection = new Thread(new RetryClientRunnable(m_clientSocket, m_nCountRetry, m_nTimeout));
        m_threadRetryConnection.setName("Retry Connection");
        m_threadRetryConnection.start();
    }

    @Override
    public void OnStopRetryThread() {
        if (m_threadRetryConnection == null) return;
        if (m_threadRetryConnection.isAlive())
            m_threadRetryConnection.interrupt();
        m_threadRetryConnection = null;
    }

    @Override
    public boolean send(String strBuffer) {
        if (m_clientSocket == null)
            return false;
        if (!m_clientSocket.isConnected()) {
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
        if (m_clientSocket == null)
            return false;
        if (!m_clientSocket.isConnected()) {
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