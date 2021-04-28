package com.yoonfactory;

public class YoonMatrix2X2Int implements IYoonMatrix<Integer> {

    public int[][] array = new int[2][2];

    public int getLength() {
        return 2;
    }

    public int getMatrix11() {
        return array[0][0];
    }

    public void setMatrix11(int nValue) {
        array[0][0] = nValue;
    }

    public int getMatrix12() {
        return array[0][1];
    }

    public void setMatrix12(int nValue) {
        array[0][1] = nValue;
    }

    public int getMatrix21() {
        return array[1][0];
    }

    public void setMatrix21(int nValue) {
        array[1][0] = nValue;
    }

    public int getMatrix22() {
        return array[1][1];
    }

    public void setMatrix22(int nValue) {
        array[1][1] = nValue;
    }

    public YoonMatrix2X2Int() {
        array[0][0] = array[1][1] = 1;
        array[0][1] = array[1][0] = 0;
    }

    public YoonMatrix2X2Int(IYoonMatrix m) {
        this.copyFrom(m);
    }

    public Integer getDeterminant() {
        return getMatrix11() * getMatrix22() - getMatrix12() * getMatrix21();
    }

    public Integer cofactor(int nRow, int nCol) {
        return 0;
    }

    public IYoonMatrix getMinorMatrix(int nRow, int nCol) {
        return new YoonMatrix2X2Int();
    }

    public IYoonMatrix getAdjointMatrix() {
        return new YoonMatrix2X2Int();
    }

    @Override
    public IYoonMatrix clone() {
        YoonMatrix2X2Int pMatrix = new YoonMatrix2X2Int();
        pMatrix.setMatrix11(getMatrix11());
        pMatrix.setMatrix12(getMatrix12());
        pMatrix.setMatrix21(getMatrix21());
        pMatrix.setMatrix22(getMatrix22());
        return pMatrix;
    }

    public void copyFrom(IYoonMatrix pMatrix) {
        if (pMatrix instanceof YoonMatrix2X2Int) {
            YoonMatrix2X2Int pObject = (YoonMatrix2X2Int) pMatrix;
            setMatrix11(pObject.getMatrix11());
            setMatrix12(pObject.getMatrix12());
            setMatrix21(pObject.getMatrix21());
            setMatrix22(pObject.getMatrix22());
        }
    }

    public IYoonMatrix inverse() {
        if (getDeterminant() == 0) return unit();

        YoonMatrix2X2Int m = new YoonMatrix2X2Int(this);
        setMatrix11(m.getMatrix22() / m.getDeterminant());
        setMatrix12(-m.getMatrix12() / m.getDeterminant());
        setMatrix21(-m.getMatrix21() / m.getDeterminant());
        setMatrix22(m.getMatrix11() / m.getDeterminant());
        return this;
    }

    public IYoonMatrix transpose() {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int(this);
        setMatrix12(m.getMatrix21());
        setMatrix21(m.getMatrix12());
        return this;
    }

    public IYoonMatrix unit() {
        array[0][0] = array[1][1] = 1;
        array[0][1] = array[1][0] = 0;
        return this;
    }

    public IYoonMatrix add(IYoonMatrix pObject) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        YoonMatrix2X2Int pMatrix = (YoonMatrix2X2Int) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = array[i][j] + pMatrix.array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix subtract(IYoonMatrix pObject) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        YoonMatrix2X2Int pMatrix = (YoonMatrix2X2Int) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = array[i][j] - pMatrix.array[i][j];
            }
        }
        return m;
    }

    public IYoonMatrix multiple(Integer nValue) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = nValue * array[i][j];
            }
        }
        return m;
    }

    public YoonMatrix2X2Int multiple(IYoonMatrix pObject) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        YoonMatrix2X2Int pMatrix = (YoonMatrix2X2Int) pObject;
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = 0;
                for (int kValue = 0; kValue < m.getLength(); kValue++)
                    m.array[i][j] += (array[i][kValue] * pMatrix.array[kValue][j]);
            }
        }
        return m;
    }

    public YoonMatrix2X2Int divide(Integer nValue) {
        YoonMatrix2X2Int m = new YoonMatrix2X2Int();
        for (int i = 0; i < m.getLength(); i++) {
            for (int j = 0; j < m.getLength(); j++) {
                m.array[i][j] = array[i][j] / nValue;
            }
        }
        return m;
    }
}