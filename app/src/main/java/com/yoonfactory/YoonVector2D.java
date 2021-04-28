package com.yoonfactory;

import static com.yoonfactory.eYoonDir2D.*;

public class YoonVector2D implements IYoonVector<Double>, IYoonVector2D<Double> {

    public double[] Array = new double[3];

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void copyFrom(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            YoonVector2D vec = (YoonVector2D) pVector;
            setX(vec.getX());
            setY(vec.getY());
            setW(1.0);
        }
    }

    @Override
    public IYoonVector clone() {
        YoonVector2D v = new YoonVector2D();
        v.setX(getX());
        v.setY(getY());
        v.setW(getW());
        return v;
    }

    @Override
    public boolean equals(IYoonFigure pObject) {
        if (pObject instanceof YoonVector2D)
            return ((YoonVector2D) pObject).Array[0] == Array[0] && ((YoonVector2D) pObject).Array[1] == Array[1] && ((YoonVector2D) pObject).Array[2] == Array[2];
        return false;
    }

    private static final double DELTA = 0.0000000000001;

    @Override
    public Double getW() {
        return Array[2];
    }

    @Override
    public void setW(Double value) {
        Array[2] = value;
    }

    @Override
    public Double getX() {
        return Array[0];
    }

    @Override
    public void setX(Double value) {
        Array[0] = value;
    }

    @Override
    public Double getY() {
        return Array[1];
    }

    @Override
    public void setY(Double value) {
        Array[1] = value;
    }

    @Override
    public eYoonDir2D getDirection() {
        double dX = Array[0];
        double dY = Array[1];
        if (dX == 0 && dY == 0)
            return Center;
        else if (dX == 0 && dY > 0)
            return Top;
        else if (dX == 0 && dY < 0)
            return Bottom;
        else if (dX > 0 && dY == 0)
            return Right;
        else if (dX < 0 && dY == 0)
            return Left;
        else if (dX > 0 && dY > 0)
            return TopRight;
        else if (dX < 0 && dY > 0)
            return TopLeft;
        else if (dX < 0 && dY < 0)
            return BottomLeft;
        else if (dX > 0 && dY < 0)
            return BottomRight;
        else
            return None;
    }

    @Override
    public void setDirection(eYoonDir2D nDir) {
        switch (nDir) {
            case Center:
                Array[0] = Array[1] = 0;
                break;
            case Top:
                Array[0] = 0;
                Array[1] = 1;
                break;
            case Bottom:
                Array[0] = 0;
                Array[1] = -1;
                break;
            case Right:
                Array[0] = 1;
                Array[1] = 0;
                break;
            case Left:
                Array[0] = -1;
                Array[1] = 0;
                break;
            case TopRight:
                Array[0] = 1;
                Array[1] = 1;
                break;
            case None:
                break;
            case TopLeft:
                Array[0] = -1;
                Array[1] = 1;
                break;
            case BottomLeft:
                Array[0] = -1;
                Array[1] = -1;
                break;
            case BottomRight:
                Array[0] = 1;
                Array[1] = -1;
                break;
            default:
                break;
        }
    }

    public YoonVector2D() {
        setX(0.0);
        setY(0.0);
        setW(1.0);
    }

    public YoonVector2D(IYoonVector p) {
        copyFrom(p);
    }

    public YoonVector2D(eYoonDir2D nDir){
        setDirection(nDir);
    }

    public YoonVector2D(double dx, double dy) {
        setX(dx);
        setY(dy);
        setW(1.0);
    }

    @Override
    public void zero() {
        setX(0.0);
        setY(0.0);
        setW(1.0);
    }

    @Override
    public double length() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    @Override
    public IYoonVector unit() {
        double len = this.length();
        if (len > DELTA) {
            len = 1.0 / len;
            Array[0] *= (int) len;
            Array[1] *= (int) len;
        }
        return this;
    }

    @Override
    public IYoonVector reverse() {
        return new YoonVector2D(-getX(), -getY());
    }

    @Override
    public double distance(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            YoonVector2D vec = (YoonVector2D) pVector;
            double dx = getX() - vec.getX();
            double dy = getY() - vec.getY();
            return Math.sqrt(dx * dx + dy * dy);
        } else
            return 0.0;
    }

    @Override
    public IYoonVector scale(Double sx, Double sy) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.setScaleUnit(sx, sy);
        YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    @Override
    public IYoonVector move(Double dx, Double dy) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.setMovementUnit(dx, dy);
        YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    @Override
    public IYoonVector move(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            YoonMatrix2D pMatrix = new YoonMatrix2D();
            pMatrix.setMovementUnit(pVector);
            YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
            setX(vec.getX());
            setY(vec.getY());
        }
        return this;
    }

    @Override
    public IYoonVector rotate(double angle) {
        YoonMatrix2D pMatrix = new YoonMatrix2D();
        pMatrix.setRotateUnit(angle);
        YoonVector2D vec = (YoonVector2D) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    @Override
    public IYoonVector rotate(IYoonVector center, double angle) {
        if (center instanceof YoonVector2D) {
            YoonVector2D pVecCenter = (YoonVector2D) center;
            move(pVecCenter.reverse());
            rotate(angle);
            move(pVecCenter);
        }
        return this;
    }

    public static YoonVector2D multiple(YoonVector2D pVector, YoonMatrix3X3Double pMatrix) {
        return (YoonVector2D) pVector.multiple(pMatrix);
    }

    public static YoonVector2D multiple(YoonVector2D pVector, double value) {
        return (YoonVector2D) pVector.multiple(value);
    }

    public static double multiple(YoonVector2D pVector1, YoonVector2D pVector2){
        return pVector1.multiple(pVector2);
    }

    public static YoonVector2D add(YoonVector2D pVector1, YoonVector2D pVector2){
        return (YoonVector2D) pVector1.add(pVector2);
    }

    public static YoonVector2D subtract(YoonVector2D pVector, YoonVector2D pObject){
        return (YoonVector2D) pVector.subtract(pObject);
    }

    public static YoonVector2D divide(YoonVector2D pVector, double value) {
        return (YoonVector2D) pVector.divide(value);
    }

    @Override
    public IYoonVector multiple(IYoonMatrix pObject) {
        YoonVector2D pVector = new YoonVector2D();
        if (pObject instanceof YoonMatrix3X3Double) {
            YoonMatrix3X3Double pMatrix = (YoonMatrix3X3Double) pObject;
            for (int i = 0; i < getCount(); i++) {
                pVector.Array[i] = 0;
                for (int k = 0; k < pMatrix.getLength(); k++)
                    pVector.Array[i] += pMatrix.array[i][k] * Array[k];
            }
        }
        return pVector;
    }

    @Override
    public IYoonVector multiple(Double value) {
        return new YoonVector2D(getX() * value, getY() * value);
    }

    @Override
    public IYoonVector add(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return new YoonVector2D(getX() + pVector.getX(), getY() + pVector.getY());
        } else
            return new YoonVector2D();
    }

    @Override
    public IYoonVector subtract(IYoonVector pObject) {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return new YoonVector2D(getX() - pVector.getX(), getY() - pVector.getY());
        } else
            return new YoonVector2D();
    }

    @Override
    public IYoonVector divide(Double value) {
        return new YoonVector2D(getX() / value, getY() / value);
    }

    @Override
    public Double multiple(IYoonVector pObject) // dot product
    {
        if (pObject instanceof YoonVector2D) {
            YoonVector2D pVector = (YoonVector2D) pObject;
            return getX() * pVector.getX() + getY() * pVector.getY();
        } else return 0.0;
    }
}