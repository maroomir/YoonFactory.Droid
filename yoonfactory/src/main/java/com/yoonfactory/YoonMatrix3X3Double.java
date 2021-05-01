package com.yoonfactory;

public class YoonMatrix3X3Double implements IYoonMatrix<Double> {
    public double[][] array = new double[3][3];

    public int getLength() {
        return 3;
    }

    public double getMatrix11() {
        return array[0][0];
    }

    public void setMatrix11(double dValue) {
        array[0][0] = dValue;
    }

    public double getMatrix12() {
        return array[0][1];
    }

    public void setMatrix12(double dValue) {
        array[0][1] = dValue;
    }

    public double getMatrix13() {
        return array[0][2];
    }

    public void setMatrix13(double dValue) {
        array[0][2] = dValue;
    }

    public double getMatrix21() {
        return array[1][0];
    }

    public void setMatrix21(double dValue) {
        array[1][0] = dValue;
    }

    public double getMatrix22() {
        return array[1][1];
    }

    public void setMatrix22(double dValue) {
        array[1][1] = dValue;
    }

    public double getMatrix23() {
        return array[1][2];
    }

    public void setMatrix23(double dValue) {
        array[1][2] = dValue;
    }

    public double getMatrix31() {
        return array[2][0];
    }

    public void setMatrix31(double dValue) {
        array[2][0] = dValue;
    }

    public double getMatrix32() {
        return array[2][1];
    }

    public void setMatrix32(double dValue) {
        array[2][1] = dValue;
    }

    public double getMatrix33() {
        return array[2][2];
    }

    public void setMatrix33(double dValue) {
        array[2][2] = dValue;
    }

    public YoonMatrix3X3Double() {
        array[0][0] = array[1][1] = array[2][2] = 1.0;
        array[0][1] = array[0][2] = array[1][0] = array[1][2] = array[2][0] = array[2][1] = 0.0;
    }

    public YoonMatrix3X3Double(IYoonMatrix pMatrix) {
        this.copyFrom(pMatrix);
    }

    public Double getDeterminant() {
        double nSum = 0;
        for (int i = 0; i < getLength(); i++)
            nSum += array[0][i] * cofactor(0, i);
        return nSum;
    }

    @Override
    public IYoonMatrix clone() {
        YoonMatrix3X3Double pMatrix = new YoonMatrix3X3Double();
        for (int i = 0; i < pMatrix.getLength(); i++) {
            for (int j = 0; j < pMatrix.getLength(); j++) {
                pMatrix.array[i][j] = array[i][j];
            }
        }
        return pMatrix;
    }

    public void copyFrom(IYoonMatrix pMatrix) {
        if (pMatrix instanceof YoonMatrix3X3Double) {
            YoonMatrix3X3Double pObject = (YoonMatrix3X3Double) pMatrix;
            for (int i = 0; i < getLength(); i++) {
                for (int j = 0; j < getLength(); j++) {
                    array[i][j] = pObject.array[i][j];
                }
            }
        }
    }

    public Double cofactor(int nRow, int nCol) {
        return Math.pow(-1, nRow + nCol) * (double) getMinorMatrix(nRow, nCol).getDeterminant();
    }

    public IYoonMatrix getMinorMatrix(int nRow, int nCol) {
        if (nRow < 0 || nRow >= getLength() || nCol < 0 || nCol >= getLength())
            return new YoonMatrix2X2Double();

        double[][] pArray = new double[2][2];
        int iCount = 0;
        int jCount = 0;
        for (int i = 0; i < getLength(); i++) {
            if (i == nRow) continue;
            for (int j = 0; j < getLength(); j++) {
                if (j == nCol) continue;
                pArray[iCount][jCount] = array[i][j];
                jCount++;
            }
            iCount++;
        }
        YoonMatrix2X2Double pMatrix = new YoonMatrix2X2Double();
        pMatrix.array = pArray;
        return pMatrix;
    }

    public IYoonMatrix getAdjointMatrix() {
        double[][] pArray = new double[3][3];
        for (int i = 0; i < getLength(); i++) {
            for (int j = 0; j < getLength(); j++) {
                pArray[i][j] = cofactor(i, j);
            }
        }
        YoonMatrix3X3Double pMatrix = new YoonMatrix3X3Double();
        pMatrix.array = pArray;
        return pMatrix.transpose();
    }

    public IYoonMatrix inverse() {
        if (getDeterminant() == 0) return unit();

        YoonMatrix3X3Double m = new YoonMatrix3X3Double(this);
        copyFrom(m.getAdjointMatrix().divide(m.getDeterminant()));
        return this;
    }

    public IYoonMatrix transpose() {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double(this);
        setMatrix12(m.getMatrix21());
        setMatrix13(m.getMatrix31());
        setMatrix21(m.getMatrix12());
        setMatrix23(m.getMatrix32());
        setMatrix31(m.getMatrix23());
        setMatrix32(m.getMatrix23());
        return this;
    }

    public IYoonMatrix unit() {
        array[0][0] = array[1][1] = array[2][2] = 1.0;
        array[0][1] = array[0][2] = array[1][0] = array[1][2] = array[2][0] = array[2][1] = 0.0;
        return this;
    }

    public IYoonMatrix add(IYoonMatrix pObject) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = array[i][j] + pMatrix.array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix subtract(IYoonMatrix pObject) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = array[i][j] - pMatrix.array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix multiple(Double dValue) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = dValue * array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix multiple(IYoonMatrix pObject) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = 0;
                for (int kValue = 0; kValue < m.getLength(); kValue++)
                    m.array[i][j] += (array[i][kValue] * pMatrix.array[kValue][j]);
            }
        }
        return m;
    }

    public IYoonMatrix divide(Double dValue) {
        YoonMatrix3X3Double m = new YoonMatrix3X3Double();
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = array[i][j] / dValue;
            }
        }
        return m;
    }
}