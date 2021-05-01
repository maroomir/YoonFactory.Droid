package com.yoonfactory;

public interface IYoonMatrix<T> extends Cloneable{

    int getLength();

    T getDeterminant();

    T cofactor(int nRow, int nCol);   // 여인자

    IYoonMatrix getMinorMatrix(int nRow, int nCol);   // 소행렬

    IYoonMatrix getAdjointMatrix();   // 수반행렬

    void copyFrom(IYoonMatrix pMatrix);

    IYoonMatrix inverse();

    IYoonMatrix transpose();

    IYoonMatrix unit();

    IYoonMatrix add(IYoonMatrix pObject);

    IYoonMatrix subtract(IYoonMatrix pObject);

    IYoonMatrix multiple(T value);

    IYoonMatrix multiple(IYoonMatrix pObject);

    IYoonMatrix divide(T value);
}