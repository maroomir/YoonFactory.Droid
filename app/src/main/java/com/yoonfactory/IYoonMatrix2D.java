package com.yoonfactory;

public interface  IYoonMatrix2D<T> extends IYoonMatrix<T> {
    IYoonMatrix setScaleUnit(T sx, T sy);

    IYoonMatrix setMovementUnit(T dx, T dy);

    IYoonMatrix setMovementUnit(IYoonVector2D<T> pVector);

    IYoonMatrix setRotateUnit(double dAngle);
}