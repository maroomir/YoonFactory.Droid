package com.yoonfactory;

import static com.yoonfactory.eYoonDir2D.*;
import static com.yoonfactory.eYoonDir2D.None;

public class YoonVector2N implements IYoonVector<Integer>, IYoonVector2D<Integer> {

    public int[] Array = new int[3];

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void copyFrom(IYoonVector pVector) {
        if (pVector instanceof YoonVector2N) {
            YoonVector2N vec = (YoonVector2N) pVector;
            setX(vec.getX());
            setY(vec.getY());
            setW(1);
        }
    }

    @Override
    public IYoonVector clone() {
        YoonVector2N v = new YoonVector2N();
        v.setX(getX());
        v.setY(getY());
        v.setW(getW());
        return v;
    }

    @Override
    public boolean equals(IYoonFigure pObject) {
        if (pObject instanceof YoonVector2N)
            return ((YoonVector2N) pObject).Array[0] == Array[0] && ((YoonVector2N) pObject).Array[1] == Array[1] && ((YoonVector2N) pObject).Array[2] == Array[2];
        return false;
    }

    private static final double DELTA = 0.0000000000001;

    @Override
    public Integer getW() {
        return Array[2];
    }

    @Override
    public void setW(Integer value) {
        Array[2] = value;
    }

    @Override
    public Integer getX() {
        return Array[0];
    }

    @Override
    public void setX(Integer value) {
        Array[0] = value;
    }

    @Override
    public Integer getY() {
        return Array[1];
    }

    @Override
    public void setY(Integer value) {
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

    public YoonVector2N() {
        setX(0);
        setY(0);
        setW(1);
    }

    public YoonVector2N(IYoonVector p) {
        copyFrom(p);
    }

    public YoonVector2N(eYoonDir2D nDir){
        setDirection(nDir);
    }

    public YoonVector2N(int dx, int dy) {
        setX(dx);
        setY(dy);
        setW(1);
    }

    @Override
    public void zero() {
        setX(0);
        setY(0);
        setW(1);
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
        return new YoonVector2N(-getX(), -getY());
    }

    @Override
    public double distance(IYoonVector pVector) {
        if (pVector instanceof YoonVector2N) {
            YoonVector2N vec = (YoonVector2N) pVector;
            double dx = getX() - vec.getX();
            double dy = getY() - vec.getY();
            return Math.sqrt(dx * dx + dy * dy);
        } else
            return 0.0;
    }

    @Override
    public IYoonVector scale(Integer sx, Integer sy) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.setScaleUnit(sx, sy);
        YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    @Override
    public IYoonVector move(Integer dx, Integer dy) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.setMovementUnit(dx, dy);
        YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    @Override
    public IYoonVector move(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            YoonMatrix2N pMatrix = new YoonMatrix2N();
            pMatrix.setMovementUnit(pVector);
            YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
            setX(vec.getX());
            setY(vec.getY());
        }
        return this;
    }

    @Override
    public IYoonVector rotate(double angle) {
        YoonMatrix2N pMatrix = new YoonMatrix2N();
        pMatrix.setRotateUnit(angle);
        YoonVector2N vec = (YoonVector2N) multiple(pMatrix);
        setX(vec.getX());
        setY(vec.getY());
        return this;
    }

    @Override
    public IYoonVector rotate(IYoonVector center, double angle) {
        if (center instanceof YoonVector2N) {
            YoonVector2N pVecCenter = (YoonVector2N) center;
            move(pVecCenter.reverse());
            rotate(angle);
            move(pVecCenter);
        }
        return this;
    }

    public static YoonVector2N multiple(YoonVector2N pVector, YoonMatrix3X3Double pMatrix) {
        return (YoonVector2N) pVector.multiple(pMatrix);
    }

    public static YoonVector2N multiple(YoonVector2N pVector, int value) {
        return (YoonVector2N) pVector.multiple(value);
    }

    public static double multiple(YoonVector2N pVector1, YoonVector2N pVector2){
        return pVector1.multiple(pVector2);
    }

    public static YoonVector2N add(YoonVector2N pVector1, YoonVector2N pVector2){
        return (YoonVector2N) pVector1.add(pVector2);
    }

    public static YoonVector2N subtract(YoonVector2N pVector, YoonVector2N pObject){
        return (YoonVector2N) pVector.subtract(pObject);
    }

    public static YoonVector2N divide(YoonVector2N pVector, int value) {
        return (YoonVector2N) pVector.divide(value);
    }

    @Override
    public IYoonVector multiple(IYoonMatrix pObject) {
        YoonVector2N pVector = new YoonVector2N();
        if (pObject instanceof YoonMatrix3X3Int) {
            YoonMatrix3X3Int pMatrix = (YoonMatrix3X3Int) pObject;
            for (int i = 0; i < getCount(); i++) {
                pVector.Array[i] = 0;
                for (int k = 0; k < pMatrix.getLength(); k++)
                    pVector.Array[i] += pMatrix.Array[i][k] * Array[k];
            }
        }
        return pVector;
    }

    @Override
    public IYoonVector multiple(Integer value) {
        return new YoonVector2N(getX() * value, getY() * value);
    }

    @Override
    public IYoonVector add(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return new YoonVector2N(getX() + pVector.getX(), getY() + pVector.getY());
        } else
            return new YoonVector2N();
    }

    @Override
    public IYoonVector subtract(IYoonVector pObject) {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return new YoonVector2N(getX() - pVector.getX(), getY() - pVector.getY());
        } else
            return new YoonVector2N();
    }

    @Override
    public IYoonVector divide(Integer value) {
        return new YoonVector2N(getX() / value, getY() / value);
    }

    @Override
    public Integer multiple(IYoonVector pObject) // dot product
    {
        if (pObject instanceof YoonVector2N) {
            YoonVector2N pVector = (YoonVector2N) pObject;
            return getX() * pVector.getX() + getY() * pVector.getY();
        } else return 0;
    }
}