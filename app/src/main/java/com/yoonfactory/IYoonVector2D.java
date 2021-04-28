package com.yoonfactory;


public interface IYoonVector2D<T> extends IYoonVector<T> {
    T getW();

    void setW(T w);

    T getX();

    void setX(T x);

    T getY();

    void setY(T y);

    eYoonDir2D getDirection();

    void setDirection(eYoonDir2D nDir);

    IYoonVector scale(T sx, T sy);

    IYoonVector move(T dx, T dy);

    IYoonVector move(IYoonVector pVector);

    IYoonVector rotate(double dAngle);

    IYoonVector rotate(IYoonVector pVecCenter, double dAngle);
}