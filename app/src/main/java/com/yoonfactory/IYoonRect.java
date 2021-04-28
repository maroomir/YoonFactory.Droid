package com.yoonfactory;

public interface IYoonRect<T> extends IYoonFigure {

    T getLeft();

    T getTop();

    T getRight();

    T getBottom();

    IYoonVector getTopLeft();

    IYoonVector getTopRight();

    IYoonVector getBottomLeft();

    IYoonVector getBottomRight();

    T area();

    boolean isContained(IYoonVector pVector);

    void copyFrom(IYoonRect pRect);
}