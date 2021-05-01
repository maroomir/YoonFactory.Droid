package com.yoonfactory;

public class YoonRect2D implements IYoonRect<Double> {

    public YoonVector2D CenterPos;
    public double Width;
    public double Height;

    @Override
    public IYoonRect clone() {
        YoonRect2D r = new YoonRect2D();
        r.CenterPos = (YoonVector2D) this.CenterPos.clone();
        r.Width = this.Width;
        r.Height = this.Height;
        return r;
    }

    @Override
    public boolean equals(IYoonFigure pObject) {
        if (pObject instanceof YoonRect2D)
            return ((YoonRect2D) pObject).Width == Width && ((YoonRect2D) pObject).Height == Height &&
                    ((YoonRect2D) pObject).CenterPos.equals(CenterPos);
        return false;
    }

    public void copyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRect2D) {
            YoonRect2D rect = (YoonRect2D) pRect;
            CenterPos = (YoonVector2D) rect.CenterPos.clone();
            Width = rect.Width;
            Height = rect.Height;
        }
    }

    public Double getLeft() {
        return CenterPos.getX() - Width / 2;
    }

    public Double getTop() {
        return CenterPos.getY() - Height / 2;
    }

    public Double getRight() {
        return CenterPos.getX() + Width / 2;
    }

    public Double getBottom() {
        return CenterPos.getY() + Height / 2;
    }

    public IYoonVector getTopLeft() {
        return new YoonVector2D(CenterPos.getX() - Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getTopRight() {
        return new YoonVector2D(CenterPos.getX() + Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getBottomLeft() {
        return new YoonVector2D(CenterPos.getX() - Width / 2, CenterPos.getY() + Height / 2);
    }

    public IYoonVector getBottomRight() {
        return new YoonVector2D(CenterPos.getX() + Width / 2, CenterPos.getY() + Height / 2);
    }

    public YoonRect2D() {
        CenterPos = new YoonVector2D();
        CenterPos.setX(0.0);
        CenterPos.setY(0.0);
        Width = 0;
        Height = 0;
    }

    public YoonRect2D(double dx, double dy, double dw, double dh) {
        CenterPos = new YoonVector2D();
        CenterPos.setX(dx);
        CenterPos.setY(dy);
        Width = dw;
        Height = dh;
    }

    public YoonRect2D(eYoonDir2D dir1, YoonVector2D pos1, eYoonDir2D dir2, YoonVector2D pos2) throws IllegalArgumentException {
        if (dir1 == eYoonDir2D.TopLeft && dir2 == eYoonDir2D.BottomRight) {
            CenterPos = YoonVector2D.divide(YoonVector2D.add(pos1, pos2), 2);
            Width = pos2.getX() - pos1.getX();
            Height = pos2.getY() - pos1.getY();
        } else if (dir1 == eYoonDir2D.BottomRight && dir2 == eYoonDir2D.TopLeft) {
            CenterPos = YoonVector2D.divide(YoonVector2D.add(pos1, pos2), 2);
            Width = pos1.getX() - pos2.getX();
            Height = pos1.getY() - pos2.getY();
        }
        if (dir1 == eYoonDir2D.TopRight && dir2 == eYoonDir2D.BottomLeft) {
            CenterPos = YoonVector2D.divide(YoonVector2D.add(pos1, pos2), 2);
            Width = pos2.getX() - pos1.getX();
            Height = pos1.getY() - pos2.getY();
        } else if (dir1 == eYoonDir2D.BottomLeft && dir2 == eYoonDir2D.TopRight) {
            CenterPos = YoonVector2D.divide(YoonVector2D.add(pos1, pos2), 2);
            Width = pos1.getX() - pos2.getX();
            Height = pos2.getY() - pos1.getY();
        } else
            throw new IllegalArgumentException();
    }

    public YoonRect2D(YoonVector2D... pArgs) {
        if (pArgs.length > 0) {
            double dMinX = Double.MAX_VALUE;
            double dMinY = Double.MAX_VALUE;
            double dMaxX = -Double.MAX_VALUE;
            double dMaxY = -Double.MAX_VALUE;
            for (int iVec = 0; iVec < pArgs.length; iVec++) {
                if (pArgs[iVec].getX() < dMinX)
                    dMinX = pArgs[iVec].getX();
                if (pArgs[iVec].getX() > dMaxX)
                    dMaxX = pArgs[iVec].getX();
                if (pArgs[iVec].getY() < dMinY)
                    dMinY = pArgs[iVec].getY();
                if (pArgs[iVec].getY() > dMaxY)
                    dMaxY = pArgs[iVec].getY();
            }
            CenterPos = new YoonVector2D((dMinX + dMaxX) / 2, (dMinY + dMaxY) / 2);
            Width = dMaxX - dMinX;
            Height = dMaxY - dMinY;
        }
    }

    public Double area() {
        return Width * Height;
    }

    @Override
    public boolean isContained(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            if (((YoonVector2D) pVector).getX() > getLeft() &&
                    ((YoonVector2D) pVector).getX() < getRight() &&
                    ((YoonVector2D) pVector).getY() > getTop() &&
                    ((YoonVector2D) pVector).getY() < getBottom())
                return true;
        }
        return false;
    }
}