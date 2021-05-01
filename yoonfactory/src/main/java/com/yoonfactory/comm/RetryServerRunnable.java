package com.yoonfactory.comm;

import com.yoonfactory.eYoonStatus;

import java.net.ServerSocket;

class RetryServerRunnable implements Runnable {

    private final ServerSocket m_serverSocket;
    private final int m_nRetryCount;
    private final int m_nTimeout;
    private boolean m_bRetryOpen = false;

    RetryServerRunnable(ServerSocket pServerSocket, int nRetryCount, int nTimeout) {
        m_serverSocket = pServerSocket;
        m_nRetryCount = nRetryCount;
        m_nTimeout = nTimeout;
    }

    void setRetryOpen(boolean bRetryOpen) {
        this.m_bRetryOpen = bRetryOpen;
    }

    @Override
    public void run() {
        if (m_serverSocket == null) return;
        long nTimeStart = System.currentTimeMillis();
        CommEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Info, "Retry Server Start");
        try {
            for (int iRetry = 0; iRetry < m_nRetryCount; iRetry++) {
                //// Error : Timeout
                if (System.currentTimeMillis() - nTimeStart >= m_nTimeout)
                    break;
                //// Error : Retry Listen is false suddenly
                if (!m_bRetryOpen)
                    break;
                ////  Success to connect
                if (m_serverSocket != null) {
                    if (m_serverSocket.isBound()) {
                        CommEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Info, "Listen Retry Success");
                        m_bRetryOpen = false;
                        break;
                    }
                }
                CommEventHandler.callRetryOpenEvent(RetryServerRunnable.class);
            }

            if (m_serverSocket == null) {
                CommEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Error, "Listen Retry Failure : Listen Socket Empty");
                return;
            }
            if (!m_serverSocket.isBound()) {
                CommEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Error, "Listen Retry Failure : Connection Fail");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            CommEventHandler.callShowMessageEvent(RetryServerRunnable.class, eYoonStatus.Info, "Retry Thread Exit");
        }
    }
}