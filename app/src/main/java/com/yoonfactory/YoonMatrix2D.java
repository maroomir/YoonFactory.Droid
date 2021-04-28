package com.yoonfactory;

public class YoonMatrix2D extends YoonMatrix3X3Double implements IYoonMatrix2D<Double>{

    public IYoonMatrix setScaleUnit(Double sx, Double sy)
    {
        unit();
        array[0][0] *= sx;
        array[1][1] *= sy;
        return this;
    }

    public IYoonMatrix setMovementUnit(Double dx, Double dy)
    {
        unit();
        array[0][2] += dx;
        array[1][2] += dy;
        return this;
    }

    public IYoonMatrix setMovementUnit(IYoonVector2D<Double> vec)
    {
        unit();
        array[0][2] += vec.getX();
        array[1][2] += vec.getY();
        return this;
    }

    public IYoonMatrix setRotateUnit(double dAngle)
    {
        unit();
        double cosT = Math.cos(dAngle);
        double sinT = Math.sin(dAngle);
        array[0][0] = cosT;
        array[0][1] = -sinT;
        array[1][0] = sinT;
        array[1][1] = cosT;
        return this;
    }
}
