package com.yoonfactory;

public  interface IYoonMatrix3D<T> extends IYoonMatrix<T> {

    IYoonMatrix setScaleUnit(T sx, T sy, T sz);

    IYoonMatrix setMovementUnit(T dx, T dy, T dz);

    IYoonMatrix setMovementUnit(IYoonVector3D<T> pVector);

    IYoonMatrix setRotateXUnit(double dAngle);

    IYoonMatrix setRotateYUnit(double dAngle);

    IYoonMatrix setRotateZUnit(double dAngle);
}