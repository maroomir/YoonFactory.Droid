package com.yoonfactory.camera;

public interface IYoonCamera {
    boolean isOpenCamera();

    boolean isStartCamera();

    boolean isLiveOn();

    int getImageWidth();

    void setImageWidth(int nWidth);

    int getImageHeight();

    void setImageHeight(int nHeight);

    int open(int nNo);

    void start();

    void liveOn();

    void liveOff();

    boolean getImage(int nTimeout);
}