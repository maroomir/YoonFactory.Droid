package com.yoonfactory;

public interface IYoonVector<T> extends IYoonFigure {
    int getCount();

    void copyFrom(IYoonVector pVector);

    void zero();

    IYoonVector unit();

    IYoonVector reverse();

    double length();

    double distance(IYoonVector pVector);

    IYoonVector add(IYoonVector pObject);

    IYoonVector subtract(IYoonVector pObject);

    IYoonVector multiple(T value);

    IYoonVector multiple(IYoonMatrix pObject);

    T multiple(IYoonVector pObject);

    IYoonVector divide(T value);
}