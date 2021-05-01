package com.yoonfactory.comm;

import com.yoonfactory.eYoonStatus;

import java.net.Socket;

class RetryClientRunnable implements Runnable {

    private final Socket m_clientSocket;
    private final int m_nRetryCount;
    private final int m_nTimeout;
    private boolean m_bRetryOpen = false;

    RetryClientRunnable(Socket pSocket, int nRetryCount, int nTimeout) {
        m_clientSocket = pSocket;
        m_nRetryCount = nRetryCount;
        m_nTimeout = nTimeout;
    }

    void setRetryOpen(boolean bRetryOpen) {
        this.m_bRetryOpen = bRetryOpen;
    }

    @Override
    public void run() {
        if (m_clientSocket == null) return;
        long nTimeStart = System.currentTimeMillis();
        CommEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Info, "Retry Client Start");
        try {
            for (int iRetry = 0; iRetry < m_nRetryCount; iRetry++) {
                //// Error : Timeout
                if (System.currentTimeMillis() - nTimeStart >= m_nTimeout)
                    break;
                //// Error : Retry Listen is false suddenly
                if (!m_bRetryOpen)
                    break;
                ////  Success to connect
                if (m_clientSocket != null) {
                    if (m_clientSocket.isConnected()) {
                        CommEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Info, "Connection Retry Success");
                        m_bRetryOpen = false;
                        break;
                    }
                }
                CommEventHandler.callRetryOpenEvent(RetryClientRunnable.class);
            }

            if (m_clientSocket == null) {
                CommEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Error, "Connection Retry Failure : Connection Socket Empty");
                return;
            }
            if (!m_clientSocket.isConnected()) {
                CommEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Error, "Connection Retry Failure : Connection Fail");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            CommEventHandler.callShowMessageEvent(RetryClientRunnable.class, eYoonStatus.Info, "Retry Thread Exit");
        }
    }
}