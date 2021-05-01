package com.yoonfactory;

import org.jetbrains.annotations.NotNull;

public class YoonRectAffine2D implements IYoonRect<Double> {

    private final YoonVector2D m_vecCornerOrigin_TopLeft = new YoonVector2D();
    private final YoonVector2D m_vecCornerOrigin_BottomLeft = new YoonVector2D();
    private final YoonVector2D m_vecCornerOrigin_TopRight = new YoonVector2D();
    private final YoonVector2D m_vecCornerOrigin_BottomRight = new YoonVector2D();
    private YoonVector2D m_vecCornerRotate_TopLeft = null;
    private YoonVector2D m_vecCornerRotate_BottomLeft = null;
    private YoonVector2D m_vecCornerRotate_TopRight = null;
    private YoonVector2D m_vecCornerRotate_BottomRight = null;
    private YoonVector2D m_vecCenter = new YoonVector2D(); // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용
    private double m_dWidth = 0.0; // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용
    private double m_dHeight = 0.0; // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용
    private double m_dRotation = 0.0; // Set 내 InitOrigin과 충돌에 따른 StackOverflow 방지용

    @Override
    public IYoonRect clone() {
        YoonRectAffine2D r = new YoonRectAffine2D(0.0, 0.0, 0.0);
        r.setCenterPos((YoonVector2D) m_vecCenter.clone());
        r.setWidth(m_dWidth);
        r.setHeight(m_dHeight);
        r.setRotation(m_dRotation);
        return r;
    }

    @Override
    public boolean equals(IYoonFigure pObject) {
        if (pObject instanceof YoonRectAffine2D)
            return ((YoonRectAffine2D) pObject).m_dWidth == m_dWidth && ((YoonRectAffine2D) pObject).m_dHeight == m_dHeight &&
                    ((YoonRectAffine2D) pObject).m_vecCenter.equals(m_vecCenter) && ((YoonRectAffine2D) pObject).m_dRotation == m_dRotation;
        return false;
    }

    public void copyFrom(IYoonRect pRect) {
        if (pRect instanceof YoonRectAffine2D) {
            YoonRectAffine2D rect = (YoonRectAffine2D) pRect;
            m_vecCenter = (YoonVector2D) rect.getCenterPos().clone();
            m_dWidth = rect.getWidth();
            m_dHeight = rect.getHeight();
            m_dRotation = rect.getRotation();
        }
    }

    public YoonVector2D getCenterPos() {
        return m_vecCenter;
    }

    public void setCenterPos(YoonVector2D value) {
        m_vecCenter = value;
        initRectOrigin(m_vecCenter, m_dWidth, m_dHeight);
    }

    public double getWidth() {
        return m_dWidth;
    }

    public void setWidth(double value) {
        m_dWidth = value;
        initRectOrigin(m_vecCenter, m_dWidth, m_dHeight);
    }

    public double getHeight() {
        return m_dHeight;
    }

    public void setHeight(double value) {
        m_dHeight = value;
        initRectOrigin(m_vecCenter, m_dWidth, m_dHeight);
    }

    public double getRotation() {
        return m_dRotation;
    }

    public void setRotation(double value) {
        m_dRotation = value;
        initRectOrigin(m_vecCenter, m_dWidth, m_dHeight);

        m_vecCornerRotate_TopLeft = (YoonVector2D) m_vecCornerOrigin_TopLeft.rotate(m_vecCenter, m_dRotation);
        m_vecCornerRotate_BottomLeft = (YoonVector2D) m_vecCornerOrigin_BottomLeft.rotate(m_vecCenter, m_dRotation);
        m_vecCornerRotate_TopRight = (YoonVector2D) m_vecCornerOrigin_TopRight.rotate(m_vecCenter, m_dRotation);
        m_vecCornerRotate_BottomRight = (YoonVector2D) m_vecCornerOrigin_BottomRight.rotate(m_vecCenter, m_dRotation);
    }

    private void initRectOrigin(YoonVector2D vecCenter, double dWidth, double dHeight) {
        m_vecCornerOrigin_TopLeft.setX(vecCenter.getX() - dWidth / 2);
        m_vecCornerOrigin_TopLeft.setY(vecCenter.getY() - dHeight / 2);
        m_vecCornerOrigin_TopRight.setX(vecCenter.getX() + dWidth / 2);
        m_vecCornerOrigin_TopRight.setY(vecCenter.getY() - dHeight / 2);
        m_vecCornerOrigin_BottomLeft.setX(vecCenter.getX() - dWidth / 2);
        m_vecCornerOrigin_BottomLeft.setY(vecCenter.getY() + dHeight / 2);
        m_vecCornerOrigin_BottomRight.setX(vecCenter.getX() + dWidth / 2);
        m_vecCornerOrigin_BottomRight.setY(vecCenter.getY() + dHeight / 2);
    }

    public Double getLeft() {
        return m_vecCenter.getX() - m_dWidth / 2;
    }

    public Double getTop() {
        return m_vecCenter.getY() - m_dHeight / 2;
    }

    public Double getRight() {
        return m_vecCenter.getX() + m_dWidth / 2;
    }

    public Double getBottom() {
        return m_vecCenter.getY() + m_dHeight / 2;
    }

    public IYoonVector getTopLeft() {
        return m_vecCornerRotate_TopLeft;

    }

    public IYoonVector getTopRight() {
        return m_vecCornerRotate_TopRight;
    }

    public IYoonVector getBottomLeft() {
        return m_vecCornerRotate_BottomLeft;
    }

    public IYoonVector getBottomRight() {
        return m_vecCornerRotate_BottomRight;
    }

    public YoonRectAffine2D(double dWidth, double dHeight, double dTheta) {
        m_vecCenter = new YoonVector2D();
        m_vecCenter.setX(0.0);
        m_vecCenter.setY(0.0);
        setWidth(dWidth);
        setHeight(dHeight);
        setRotation(dTheta);
    }

    public YoonRectAffine2D(double dX, double dY, double dWidth, double dHeight, double dTheta) {
        m_vecCenter = new YoonVector2D();
        m_vecCenter.setX(dX);
        m_vecCenter.setY(dY);
        setWidth(dWidth);
        setHeight(dHeight);
        setRotation(dTheta);
    }

    public YoonRectAffine2D(@NotNull YoonVector2D vecPos, double dWidth, double dHeight, double dTheta) {
        m_vecCenter = (YoonVector2D) vecPos.clone();
        setWidth(dWidth);
        setHeight(dHeight);
        setRotation(dTheta);
    }

    public Double area() {
        return m_dWidth * m_dHeight;
    }

    @Override
    public boolean isContained(IYoonVector pVector) {
        if (pVector instanceof YoonVector2D) {
            if (m_vecCornerRotate_BottomLeft.getX() < ((YoonVector2D) pVector).getX() &&
                    m_vecCornerRotate_TopLeft.getX() < ((YoonVector2D) pVector).getX() &&
                    ((YoonVector2D) pVector).getX() < m_vecCornerRotate_BottomRight.getX() &&
                    ((YoonVector2D) pVector).getX() < m_vecCornerRotate_TopRight.getX() &&
                    m_vecCornerRotate_TopLeft.getY() < ((YoonVector2D) pVector).getY() &&
                    m_vecCornerRotate_TopRight.getY() < ((YoonVector2D) pVector).getY() &&
                    ((YoonVector2D) pVector).getY() < m_vecCornerRotate_BottomLeft.getY() &&
                    ((YoonVector2D) pVector).getY() < m_vecCornerRotate_BottomRight.getY())
                return true;
        }
        return false;
    }
}