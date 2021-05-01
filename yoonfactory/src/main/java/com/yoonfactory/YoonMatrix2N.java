package com.yoonfactory;

public class YoonMatrix2N extends YoonMatrix3X3Int implements IYoonMatrix2D<Integer>{

    public IYoonMatrix setScaleUnit(Integer sx, Integer sy)
    {
        unit();
        Array[0][0] *= sx;
        Array[1][1] *= sy;
        return this;
    }

    public IYoonMatrix setMovementUnit(Integer dx, Integer dy)
    {
        unit();
        Array[0][2] += dx;
        Array[1][2] += dy;
        return this;
    }

    public IYoonMatrix setMovementUnit(IYoonVector2D<Integer> vec)
    {
        unit();
        Array[0][2] += vec.getX();
        Array[1][2] += vec.getY();
        return this;
    }

    public IYoonMatrix setRotateUnit(double dAngle)
    {
        unit();
        int cosT = (int)Math.cos(dAngle);
        int sinT = (int)Math.sin(dAngle);
        Array[0][0] = cosT;
        Array[0][1] = -sinT;
        Array[1][0] = sinT;
        Array[1][1] = cosT;
        return this;
    }
}
