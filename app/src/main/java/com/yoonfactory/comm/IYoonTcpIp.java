package com.yoonfactory.comm;

public interface IYoonTcpIp extends IYoonComm {
    boolean isRetryOpen();

    boolean isConnected();

    String getAddress();

    void setAddress(String strAddress);

    int getPort();

    void setPort(int nPort);

    int getRetryCount();

    int getTimeout();

    void OnStartRetryThread();

    void OnStopRetryThread();
}
