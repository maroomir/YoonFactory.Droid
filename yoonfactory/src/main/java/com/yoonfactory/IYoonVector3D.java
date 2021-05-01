package com.yoonfactory;

public interface IYoonVector3D<T> extends IYoonVector<T>{
    T getW();

    void setW();

    T getX();

    void setX();

    T getY();

    void setY();

    T getZ();

    void setZ();

    IYoonVector scale(T sx, T sy, T sz);

    IYoonVector move(T dx, T dy, T dz);

    IYoonVector move(IYoonVector pVector);

    IYoonVector rotateX(double dAngle);

    IYoonVector rotateY(double dAngle);

    IYoonVector rotateZ(double dAngle);
}