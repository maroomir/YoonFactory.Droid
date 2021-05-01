package com.yoonfactory.comm;

public interface IYoonComm {

    StringBuilder getReceiveMessage();

    boolean isSend();

    void copyFrom(IYoonComm pComm);

    boolean open();

    void close();

    boolean send(String strBuffer);

    boolean send(byte[] pBuffer);
}
