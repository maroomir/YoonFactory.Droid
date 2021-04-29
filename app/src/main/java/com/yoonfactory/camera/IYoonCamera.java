package com.yoonfactory.camera;

public interface IYoonCamera {
    boolean isOpenCamera();

    boolean isStartCamera();

    boolean isLiveOn();

    int getImageWidth();

    void setImageWidth(int nWidth);

    int getImageHeight();

    void setImageHeight(int nHeight);

    void open();

    void start();

    void liveOn();

    void liveOff();

    boolean getImage(int nTimeout);
}