package com.yoonfactory.image;

import com.yoonfactory.IYoonFigure;

public class YoonObject<T extends IYoonFigure> implements Cloneable {
    private final int DEFAULT_LABEL = 0;
    private final int DEFAULT_PIX_COUNT = 0;
    private final double DEFAULT_SCORE = 0.0;

    private int m_nLabel;
    private double m_dScore;
    private T m_pObject;
    private int m_nPixelCount;

    public int getLabel() {
        return m_nLabel;
    }

    public void setLabel(int nLabel) {
        this.m_nLabel = nLabel;
    }

    public double getScore() {
        return m_dScore;
    }

    public void setScore(double dScore) {
        this.m_dScore = dScore;
    }

    public T getObject() {
        return m_pObject;
    }

    public void setObject(T pObject) {
        this.m_pObject = pObject;
    }

    public int getPixelCount() {
        return m_nPixelCount;
    }

    public void setM_nPixelCount(int nPixelCount) {
        this.m_nPixelCount = nPixelCount;
    }

    public YoonObject(T pObject) {
        m_nLabel = DEFAULT_LABEL;
        m_dScore = DEFAULT_SCORE;
        m_nPixelCount = DEFAULT_PIX_COUNT;
        m_pObject = (T) pObject.clone();
    }

    public YoonObject(int nLabel, T pObject) {
        m_nLabel = nLabel;
        m_dScore = DEFAULT_SCORE;
        m_nPixelCount = DEFAULT_PIX_COUNT;
        m_pObject = (T) pObject.clone();
    }

    public YoonObject(int nLabel, T pObject, int nCount) {
        m_nLabel = nLabel;
        m_dScore = DEFAULT_SCORE;
        m_nPixelCount = nCount;
        m_pObject = (T) pObject.clone();
    }

    public YoonObject(int nLabel, T pObject, double dScore, int nCount) {
        m_nLabel = nLabel;
        m_dScore = dScore;
        m_nPixelCount = nCount;
        m_pObject = (T) pObject.clone();
    }

    public boolean equals(YoonObject pObject) {
        if (pObject.m_nLabel == this.m_nLabel &&
                pObject.m_nPixelCount == this.m_nPixelCount &&
                pObject.m_pObject.equals(m_pObject) &&
                pObject.m_dScore == this.m_dScore)
            return true;
        return false;
    }

    public void CopyFrom(YoonObject pObject) {
        m_nLabel = pObject.m_nLabel;
        m_dScore = pObject.m_dScore;
        m_nPixelCount = pObject.m_nPixelCount;
        m_pObject = (T) pObject.m_pObject.clone();
    }

    public YoonObject Clone() {
        return new YoonObject(m_nLabel, m_pObject, m_dScore, m_nPixelCount);
    }
}
