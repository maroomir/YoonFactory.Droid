package com.yoonfactory;

public interface IYoonTriangle<T> extends IYoonFigure {
    T getX();

    T setX(T x);

    T getY();

    T setY(T y);

    T getHeight();

    T setHeight(T height);

    T area();

    void copyFrom(IYoonTriangle pTriangle);
}
