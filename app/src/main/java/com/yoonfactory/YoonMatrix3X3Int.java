package com.yoonfactory;

public class YoonMatrix3X3Int implements IYoonMatrix<Integer> {
    public int[][] Array = new int[3][3];

    public int getLength() {
        return 3;
    }

    public int getMatrix11() {
        return Array[0][0];
    }

    public void setMatrix11(int nValue) {
        Array[0][0] = nValue;
    }

    public int getMatrix12() {
        return Array[0][1];
    }

    public void setMatrix12(int nValue) {
        Array[0][1] = nValue;
    }

    public int getMatrix13() {
        return Array[0][2];
    }

    public void setMatrix13(int nValue) {
        Array[0][2] = nValue;
    }

    public int getMatrix21() {
        return Array[1][0];
    }

    public void setMatrix21(int nValue) {
        Array[1][0] = nValue;
    }

    public int getMatrix22() {
        return Array[1][1];
    }

    public void setMatrix22(int nValue) {
        Array[1][1] = nValue;
    }

    public int getMatrix23() {
        return Array[1][2];
    }

    public void setMatrix23(int nValue) {
        Array[1][2] = nValue;
    }

    public int getMatrix31() {
        return Array[2][0];
    }

    public void setMatrix31(int nValue) {
        Array[2][0] = nValue;
    }

    public int getMatrix32() {
        return Array[2][1];
    }

    public void setMatrix32(int nValue) {
        Array[2][1] = nValue;
    }

    public int getMatrix33() {
        return Array[2][2];
    }

    public void setMatrix33(int nValue) {
        Array[2][2] = nValue;
    }

    public YoonMatrix3X3Int() {
        Array[0][0] = Array[1][1] = Array[2][2] = 1;
        Array[0][1] = Array[0][2] = Array[1][0] = Array[1][2] = Array[2][0] = Array[2][1] = 0;
    }

    public YoonMatrix3X3Int(IYoonMatrix pMatrix) {
        this.copyFrom(pMatrix);
    }

    public Integer getDeterminant() {
        int nSum = 0;
        for (int i = 0; i < getLength(); i++)
            nSum += Array[0][i] * cofactor(0, i);
        return nSum;
    }

    @Override
    public IYoonMatrix clone() {
        YoonMatrix3X3Int pMatrix = new YoonMatrix3X3Int();
        for (int i = 0; i < pMatrix.getLength(); i++) {
            for (int j = 0; j < pMatrix.getLength(); j++) {
                pMatrix.Array[i][j] = Array[i][j];
            }
        }
        return pMatrix;
    }

    public void copyFrom(IYoonMatrix pMatrix) {
        if (pMatrix instanceof YoonMatrix3X3Int) {
            YoonMatrix3X3Int pObject = (YoonMatrix3X3Int) pMatrix;
            for (int i = 0; i < getLength(); i++) {
                for (int j = 0; j < getLength(); j++) {
                    Array[i][j] = pObject.Array[i][j];
                }
            }
        }
    }

    public Integer cofactor(int nRow, int nCol) {
        return (int) Math.pow(-1, nRow + nCol) * (int) getMinorMatrix(nRow, nCol).getDeterminant();
    }

    public IYoonMatrix getMinorMatrix(int nRow, int nCol) {
        if (nRow < 0 || nRow >= getLength() || nCol < 0 || nCol >= getLength())
            return new YoonMatrix2X2Int();

        int[][] pArray = new int[2][2];
        int iCount = 0;
        int jCount = 0;
        for (int i = 0; i < getLength(); i++) {
            if (i == nRow) continue;
            for (int j = 0; j < getLength(); j++) {
                if (j == nCol) continue;
                pArray[iCount][jCount] = Array[i][j];
                jCount++;
            }
            iCount++;
        }
        YoonMatrix2X2Int pMatrix = new YoonMatrix2X2Int();
        pMatrix.array = pArray;
        return pMatrix;
    }

    public IYoonMatrix getAdjointMatrix() {
        int[][] pArray = new int[3][3];
        for (int i = 0; i < getLength(); i++) {
            for (int j = 0; j < getLength(); j++) {
                pArray[i][j] = cofactor(i, j);
            }
        }
        YoonMatrix3X3Int pMatrix = new YoonMatrix3X3Int();
        pMatrix.Array = pArray;
        return pMatrix.transpose();
    }

    public IYoonMatrix inverse() {
        if (getDeterminant() == 0) return unit();

        YoonMatrix3X3Int m = new YoonMatrix3X3Int(this);
        copyFrom(m.getAdjointMatrix().divide(m.getDeterminant()));
        return this;
    }

    public IYoonMatrix transpose() {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int(this);
        setMatrix12(m.getMatrix21());
        setMatrix13(m.getMatrix31());
        setMatrix21(m.getMatrix12());
        setMatrix23(m.getMatrix32());
        setMatrix31(m.getMatrix23());
        setMatrix32(m.getMatrix23());
        return this;
    }

    public IYoonMatrix unit() {
        Array[0][0] = Array[1][1] = Array[2][2] = 1;
        Array[0][1] = Array[0][2] = Array[1][0] = Array[1][2] = Array[2][0] = Array[2][1] = 0;
        return this;
    }

    public IYoonMatrix add(IYoonMatrix pObject) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.Array[i][j] = Array[i][j] + pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix subtract(IYoonMatrix pObject) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.Array[i][j] = Array[i][j] - pMatrix.Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix multiple(Integer nValue) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.Array[i][j] = nValue * Array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix multiple(IYoonMatrix pObject) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.Array[i][j] = 0;
                for (int kValue = 0; kValue < m.getLength(); kValue++)
                    m.Array[i][j] += (Array[i][kValue] * pMatrix.Array[kValue][j]);
            }
        }
        return m;
    }

    public IYoonMatrix divide(Integer nValue) {
        YoonMatrix3X3Int m = new YoonMatrix3X3Int();
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.Array[i][j] = Array[i][j] / nValue;
            }
        }
        return m;
    }
}