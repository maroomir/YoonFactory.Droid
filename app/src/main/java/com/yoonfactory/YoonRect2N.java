package com.yoonfactory;

public class YoonRect2N implements IYoonRect<Integer> {

    public YoonVector2N CenterPos;
    public int Width;
    public int Height;

    @Override
    public IYoonRect clone() {
        YoonRect2N r = new YoonRect2N();
        r.CenterPos = (YoonVector2N) this.CenterPos.clone();
        r.Width = this.Width;
        r.Height = this.Height;
        return r;
    }

    @Override
    public boolean equals(IYoonFigure pObject) {
        if (pObject instanceof YoonRect2N)
            return ((YoonRect2N) pObject).CenterPos.equals(CenterPos) && ((YoonRect2N) pObject).Width == Width && ((YoonRect2N) pObject).Height == Height;
        return false;
    }

    public void copyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRect2N) {
            YoonRect2N rect = (YoonRect2N) pRect;
            CenterPos = (YoonVector2N) rect.CenterPos.clone();
            Width = rect.Width;
            Height = rect.Height;
        }
    }

    public Integer getLeft() {
        return CenterPos.getX() - Width / 2;
    }

    public Integer getTop() {
        return CenterPos.getY() - Height / 2;
    }

    public Integer getRight() {
        return CenterPos.getX() + Width / 2;
    }

    public Integer getBottom() {
        return CenterPos.getY() + Height / 2;
    }

    public IYoonVector getTopLeft() {
        return new YoonVector2N(CenterPos.getX() - Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getTopRight() {
        return new YoonVector2N(CenterPos.getX() + Width / 2, CenterPos.getY() - Height / 2);
    }

    public IYoonVector getBottomLeft() {
        return new YoonVector2N(CenterPos.getX() - Width / 2, CenterPos.getY() + Height / 2);
    }

    public IYoonVector getBottomRight() {
        return new YoonVector2N(CenterPos.getX() + Width / 2, CenterPos.getY() + Height / 2);
    }

    public YoonRect2N() {
        CenterPos = new YoonVector2N();
        CenterPos.setX(0);
        CenterPos.setY(0);
        Width = 0;
        Height = 0;
    }

    public YoonRect2N(int dx, int dy, int dw, int dh) {
        CenterPos = new YoonVector2N();
        CenterPos.setX(dx);
        CenterPos.setY(dy);
        Width = dw;
        Height = dh;
    }

    public YoonRect2N(eYoonDir2D dir1, YoonVector2N pos1, eYoonDir2D dir2, YoonVector2N pos2) throws IllegalArgumentException {
        if (dir1 == eYoonDir2D.TopLeft && dir2 == eYoonDir2D.BottomRight) {
            CenterPos = YoonVector2N.divide(YoonVector2N.add(pos1, pos2), 2);
            Width = pos2.getX() - pos1.getX();
            Height = pos2.getY() - pos1.getY();
        } else if (dir1 == eYoonDir2D.BottomRight && dir2 == eYoonDir2D.TopLeft) {
            CenterPos = YoonVector2N.divide(YoonVector2N.add(pos1, pos2), 2);
            Width = pos1.getX() - pos2.getX();
            Height = pos1.getY() - pos2.getY();
        }
        if (dir1 == eYoonDir2D.TopRight && dir2 == eYoonDir2D.BottomLeft) {
            CenterPos = YoonVector2N.divide(YoonVector2N.add(pos1, pos2), 2);
            Width = pos2.getX() - pos1.getX();
            Height = pos1.getY() - pos2.getY();
        } else if (dir1 == eYoonDir2D.BottomLeft && dir2 == eYoonDir2D.TopRight) {
            CenterPos = YoonVector2N.divide(YoonVector2N.add(pos1, pos2), 2);
            Width = pos1.getX() - pos2.getX();
            Height = pos2.getY() - pos1.getY();
        } else
            throw new IllegalArgumentException();
    }

    public YoonRect2N(YoonVector2N... pArgs) {
        if (pArgs.length > 0) {
            int nMinX = Integer.MAX_VALUE;
            int nMinY = Integer.MAX_VALUE;
            int nMaxX = -Integer.MAX_VALUE;
            int nMaxY = -Integer.MAX_VALUE;
            for (int iVec = 0; iVec < pArgs.length; iVec++) {
                if (pArgs[iVec].getX() < nMinX)
                    nMinX = pArgs[iVec].getX();
                if (pArgs[iVec].getX() > nMaxX)
                    nMaxX = pArgs[iVec].getX();
                if (pArgs[iVec].getY() < nMinY)
                    nMinY = pArgs[iVec].getY();
                if (pArgs[iVec].getY() > nMaxY)
                    nMaxY = pArgs[iVec].getY();
            }
            CenterPos = new YoonVector2N((nMinX + nMaxX) / 2, (nMinY + nMaxY) / 2);
            Width = nMaxX - nMinX;
            Height = nMaxY - nMinY;
        }
    }

    public Integer area() {
        return Width * Height;
    }

    @Override
    public boolean isContained(IYoonVector pVector) {
        if (pVector instanceof YoonVector2N) {
            if (((YoonVector2N) pVector).getX() > getLeft() &&
                    ((YoonVector2N) pVector).getX() < getRight() &&
                    ((YoonVector2N) pVector).getY() > getTop() &&
                    ((YoonVector2N) pVector).getY() < getBottom())
                return true;
        }
        return false;
    }
}