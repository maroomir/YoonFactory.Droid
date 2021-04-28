package com.yoonfactory;

public enum eYoonDir2D {
    None(-1) {
        @Override
        public eYoonDir2D nextQuadrant() {
            return this;
        }

        @Override
        public eYoonDir2D prevQuadrant() {
            return this;
        }

        @Override
        public eYoonDir2D nextOctant() {
            return this;
        }

        @Override
        public eYoonDir2D prevOctant() {
            return this;
        }

        @Override
        public eYoonDir2D reverseX() {
            return this;
        }

        @Override
        public eYoonDir2D reverseY() {
            return this;
        }

        @Override
        public eYoonDir2D nextOrder() {
            return this;
        }

        @Override
        public eYoonDir2D prevOrder() {
            return this;
        }

        @Override
        public eYoonDir2D nextWhirlpool() {
            return this;
        }

        @Override
        public eYoonDir2D prevWhirlpool() {
            return this;
        }

        @Override
        public int toClock() {
            return -1;
        }
    },
    TopLeft(0) {
        @Override
        public eYoonDir2D nextQuadrant() {
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D prevQuadrant() {
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D nextOctant() {
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D prevOctant() {
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D reverseX() {
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D reverseY() {
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D nextOrder() {
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D prevOrder() {
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D nextWhirlpool() {
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D prevWhirlpool() {
            return eYoonDir2D.Center;
        }

        @Override
        public int toClock() {
            return 11;
        }
    },
    Top(1){
        @Override
        public eYoonDir2D nextQuadrant(){
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D prevQuadrant(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D nextOctant(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D prevOctant(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D reverseX(){
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D reverseY(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D nextOrder(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D prevOrder(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public int toClock() {
            return 0;
        }
    },
    TopRight(2){
        @Override
        public eYoonDir2D nextQuadrant(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D prevQuadrant(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D nextOctant(){
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D prevOctant(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D reverseX(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D reverseY(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D nextOrder(){
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D prevOrder(){
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.Top;
        }

        @Override
        public int toClock() {
            return 1;
        }
    },
    Left(3){
        @Override
        public eYoonDir2D nextQuadrant(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D prevQuadrant(){
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D nextOctant(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D prevOctant(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D reverseX(){
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D reverseY(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D nextOrder(){
            return eYoonDir2D.Center;
        }

        @Override
        public eYoonDir2D prevOrder(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.Center;
        }

        @Override
        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public int toClock() {
            return 9;
        }
    },
    Center(4){
        @Override
        public eYoonDir2D nextQuadrant() {
            return this;
        }

        @Override
        public eYoonDir2D prevQuadrant() {
            return this;
        }

        @Override
        public eYoonDir2D nextOctant() {
            return this;
        }

        @Override
        public eYoonDir2D prevOctant() {
            return this;
        }

        @Override
        public eYoonDir2D reverseX() {
            return this;
        }

        @Override
        public eYoonDir2D reverseY() {
            return this;
        }

        public eYoonDir2D nextOrder(){
            return eYoonDir2D.Right;
        }

        public eYoonDir2D prevOrder(){
            return eYoonDir2D.Left;
        }

        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.TopLeft;
        }

        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.Left;
        }

        @Override
        public int toClock() {
            return 0;
        }
    },
    Right(5){
        @Override
        public eYoonDir2D nextQuadrant(){
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D prevQuadrant(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D nextOctant(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D prevOctant(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D reverseX(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D reverseY(){
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D nextOrder(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D prevOrder(){
            return eYoonDir2D.Center;
        }

        @Override
        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public int toClock() {
            return 3;
        }
    },
    BottomLeft(6){
        @Override
        public eYoonDir2D nextQuadrant(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D prevQuadrant(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D nextOctant(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D prevOctant(){
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D reverseX(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D reverseY(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D nextOrder(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D prevOrder(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public int toClock() {
            return 7;
        }
    },
    Bottom(7){
        @Override
        public eYoonDir2D nextQuadrant(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D prevQuadrant(){
            return eYoonDir2D.Left;
        }

        @Override
        public eYoonDir2D nextOctant(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D prevOctant(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D reverseX(){
            return eYoonDir2D.Top;
        }

        @Override
        public eYoonDir2D reverseY(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D nextOrder(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public eYoonDir2D prevOrder(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.BottomRight;
        }

        @Override
        public int toClock() {
            return 6;
        }
    },
    BottomRight(8){
        @Override
        public eYoonDir2D nextQuadrant(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D prevQuadrant(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D nextOctant(){
            return eYoonDir2D.Right;
        }

        @Override
        public eYoonDir2D prevOctant(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D reverseX(){
            return eYoonDir2D.TopRight;
        }

        @Override
        public eYoonDir2D reverseY(){
            return eYoonDir2D.BottomLeft;
        }

        @Override
        public eYoonDir2D nextOrder(){
            return eYoonDir2D.TopLeft;
        }

        @Override
        public eYoonDir2D prevOrder(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D nextWhirlpool(){
            return eYoonDir2D.Bottom;
        }

        @Override
        public eYoonDir2D prevWhirlpool(){
            return eYoonDir2D.Right;
        }

        @Override
        public int toClock() {
            return 5;
        }
    };

    public abstract eYoonDir2D nextQuadrant();
    public abstract eYoonDir2D prevQuadrant();
    public abstract eYoonDir2D nextOctant();
    public abstract eYoonDir2D prevOctant();
    public abstract eYoonDir2D reverseX();
    public abstract eYoonDir2D reverseY();
    public abstract eYoonDir2D nextOrder();
    public abstract eYoonDir2D prevOrder();
    public abstract eYoonDir2D nextWhirlpool();
    public abstract eYoonDir2D prevWhirlpool();
    public abstract int toClock();

    private final int m_nOrder;

    private eYoonDir2D(int nOrder) {
        m_nOrder = nOrder;
    }

    public eYoonDir2D go(eYoonDirMode nMode) {
        switch (nMode) {
            case Fixed:
                return this;
            case Clock4:
                return this.prevQuadrant();
            case AntiClock4:
                return this.nextQuadrant();
            case Clock8:
                return this.prevOctant();
            case AntiClock8:
                return this.nextOctant();
            case Increase:
                return this.nextOrder();
            case Decrease:
                return this.prevOrder();
            case Whirlpool:
                return this.nextWhirlpool();
            case AntiWhirlpool:
                return this.prevWhirlpool();
            case AxisX:
                return this.reverseY();
            case AxisY:
                return this.reverseX();
            default:
                return eYoonDir2D.None;
        }
    }

    public static eYoonDir2D[] getClockDirections() {
        eYoonDir2D[] pDirs = {Right, TopRight, Top, TopLeft, Left, BottomLeft, Bottom, BottomRight};
        return pDirs;
    }

    public static eYoonDir2D[] getSquareDirections() {
        eYoonDir2D[] pDirs = {TopRight, TopLeft, BottomLeft, BottomRight};
        return pDirs;
    }

    public static eYoonDir2D[] getRhombusDirections() {
        eYoonDir2D[] pDirs = {Top, Left, Bottom, Right};
        return pDirs;
    }

    public static eYoonDir2D[] getHorizonDirections() {
        eYoonDir2D[] pDirs = {Left, Right};
        return pDirs;
    }

    public static eYoonDir2D[] getVerticalDirections(){
        eYoonDir2D[] pDirs = {Top, Bottom};
        return pDirs;
    }
}