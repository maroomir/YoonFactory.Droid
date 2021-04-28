package com.yoonfactory.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.yoonfactory.YoonRect2N;
import com.yoonfactory.YoonVector2N;
import com.yoonfactory.file.FileFactory;
import com.yoonfactory.file.IYoonFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class YoonImage implements IYoonFile {
    public final int DEFAULT_WIDTH = 640;
    public final int DEFAULT_HEIGHT = 480;
    protected Bitmap m_pImage = null;
    protected String m_strFilePath = "";

    @Override
    public String getFilePath() {
        return m_strFilePath;
    }

    public void setFilePath(String strFilePath) {
        this.m_strFilePath = strFilePath;
    }

    public YoonImage() {
        m_pImage = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ALPHA_8);
    }

    public YoonImage(Bitmap pImage) {
        m_pImage = pImage;
    }

    public YoonImage(int nWidth, int nHeight, int nPlane) {
        switch (nPlane) {
            case 1:
                m_pImage = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ALPHA_8);
            case 2:
                m_pImage = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.RGB_565);
            case 4:
                m_pImage = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.RGBA_F16);
            default:
                throw new IllegalArgumentException();
        }
    }

    public YoonImage(byte[] pBuffer, int nWidth, int nHeight) {
        fromByteArray(pBuffer, nWidth, nHeight);
    }

    public YoonImage(byte[] pBuffer, int nWidth, int nHeight, int nPlane){
        fromByteArray(pBuffer, nWidth, nHeight, nPlane);
    }

    public YoonImage(int[] pBuffer, int nWidth, int nHeight) {
        fromIntegerArray(pBuffer, nWidth, nHeight);
    }

    public int getWidth() {
        return m_pImage.getWidth();
    }

    public int getHeight() {
        return m_pImage.getHeight();
    }

    public int getStride() {
        return m_pImage.getWidth() * getPlane();
    }

    public int getPlane() {
        switch (m_pImage.getConfig()) {
            case ALPHA_8:
                return 1;
            case RGB_565:
                return 2;
            case ARGB_8888:
            case RGBA_F16:
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public void copyFrom(IYoonFile pFile) {
        if (pFile instanceof YoonImage) {
            YoonImage pImage = (YoonImage) pFile;
            m_strFilePath = pImage.getFilePath();
            m_pImage = pImage.copyImage();
        }
    }

    @Override
    public boolean isFileExist() {
        return FileFactory.isFileExist(m_strFilePath);
    }

    @Override
    public boolean loadFile() {
        return loadImage(m_strFilePath);
    }

    @Override
    public boolean saveFile() {
        return saveImage(m_strFilePath);
    }

    public boolean loadImage(String strImagePath) {
        m_strFilePath = strImagePath;
        m_pImage = BitmapFactory.decodeFile(m_strFilePath);
        if (m_pImage == null) {
            m_pImage = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ALPHA_8);
            return false;
        }
        return true;
    }

    public boolean saveImage(String strImagePath) {
        if (m_pImage == null) return false;
        File pFile = new File(strImagePath);
        try {
            pFile.createNewFile();
            FileOutputStream pStream = new FileOutputStream(pFile);
            m_pImage.compress(Bitmap.CompressFormat.valueOf(strImagePath), 100, pStream);
            pStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isVerifiedArea(YoonRect2N pArea) {
        return pArea.getLeft() >= 0 && pArea.getTop() >= 0 && pArea.getRight() <= m_pImage.getWidth() && pArea.getBottom() <= m_pImage.getHeight();
    }

    public Bitmap copyImage() {
        return Bitmap.createBitmap(m_pImage);
    }

    public YoonImage cropImage(YoonRect2N pArea) {
        if (m_pImage == null) throw new NullPointerException();
        if (!isVerifiedArea(pArea)) throw new IllegalArgumentException();
        Bitmap pImageCrop = Bitmap.createBitmap(m_pImage, pArea.getLeft(), pArea.getTop(), pArea.Width, pArea.Height);
        return new YoonImage(pImageCrop);
    }

    public void drawRect(YoonRect2N pRect, Color penColor, int nPenWidth, float dRatio) {
        if (pRect.getRight() <= pRect.getLeft() || pRect.getBottom() <= pRect.getTop())
            return;
        drawLine((YoonVector2N) pRect.getTopLeft(), (YoonVector2N) pRect.getTopRight(), penColor, nPenWidth, dRatio);
        drawLine((YoonVector2N) pRect.getTopRight(), (YoonVector2N) pRect.getBottomRight(), penColor, nPenWidth, dRatio);
        drawLine((YoonVector2N) pRect.getBottomLeft(), (YoonVector2N) pRect.getBottomRight(), penColor, nPenWidth, dRatio);
        drawLine((YoonVector2N) pRect.getTopLeft(), (YoonVector2N) pRect.getBottomLeft(), penColor, nPenWidth, dRatio);
    }

    public void drawLine(YoonVector2N vecPos1, YoonVector2N vecPos2, Color penColor, int penWidth, float dRatio) {
        Canvas pCanvas = new Canvas(m_pImage);
        Paint pPaint = new Paint();
        pPaint.setColor(penColor.toArgb());
        pPaint.setStrokeWidth(penWidth);
        pCanvas.drawLine(vecPos1.getX() * dRatio, vecPos1.getY() * dRatio, vecPos2.getX() * dRatio, vecPos2.getY() * dRatio, pPaint);
    }

    public void drawLine(int x, int y, int x1, int y1, Color penColor, int penWidth, float dRatio) {
        Canvas pCanvas = new Canvas(m_pImage);
        Paint pPaint = new Paint();
        pPaint.setColor(penColor.toArgb());
        pPaint.setStrokeWidth(penWidth);
        pCanvas.drawLine(x * dRatio, y * dRatio, x1 * dRatio, y1 * dRatio, pPaint);
    }

    public void drawLine(float x, float y, float x1, float y1, Color penColor, int penWidth, float dRatio) {
        Canvas pCanvas = new Canvas(m_pImage);
        Paint pPaint = new Paint();
        pPaint.setColor(penColor.toArgb());
        pPaint.setStrokeWidth(penWidth);
        pCanvas.drawLine(x * dRatio, y * dRatio, x1 * dRatio, y1 * dRatio, pPaint);
    }

    public void drawText(YoonVector2N vecPos, Color fontColor, String text, int fontSize, float dRatio) {
        Canvas pCanvas = new Canvas(m_pImage);
        Paint pPaint = new Paint();
        pPaint.setColor(fontColor.toArgb());
        pPaint.setTextSize(fontSize);
        pCanvas.drawText(text, vecPos.getX() * dRatio, vecPos.getY() * dRatio, pPaint);
    }

    public void drawCross(YoonVector2N vecPos, Color penColor, int crossSize, int penWidth, float dRatio) {
        float x1, x2, y1, y2;
        x1 = vecPos.getX() * dRatio - crossSize;
        x2 = vecPos.getX() * dRatio + crossSize;
        y1 = vecPos.getY() * dRatio - crossSize;
        y2 = vecPos.getY() * dRatio + crossSize;
        drawLine(x1, vecPos.getY(), x2, vecPos.getY(), penColor, penWidth, 1.0F);
        drawLine(vecPos.getX(), y1, vecPos.getX(), y2, penColor, penWidth, 1.0F);
    }

    public byte[] toByteArray() {
        if (m_pImage == null) throw new NullPointerException();
        ByteArrayOutputStream pStream = new ByteArrayOutputStream();
        m_pImage.compress(Bitmap.CompressFormat.JPEG, 100, pStream);
        return pStream.toByteArray();
    }

    public int[] toIntegerArray() throws IOException {
        if (m_pImage == null) throw new NullPointerException();
        if (getPlane() == 4) {
            int nX = m_pImage.getWidth();
            int nY = m_pImage.getHeight();
            int[] pResult = new int[nX * nY];
            m_pImage.getPixels(pResult, 0, nX, 0, 0, nX, nY);
        }
        throw new IOException();
    }

    public boolean fromByteArray(byte[] pArray, int nWidth, int nHeight) {
        try {
            m_pImage = BitmapFactory.decodeByteArray(pArray, 0, nWidth * nHeight);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            m_pImage = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ALPHA_8);
            return false;
        }
    }

    public boolean fromByteArray(byte[] pArray, int nWidth, int nHeight, int nPlane) {
        try {
            m_pImage = BitmapFactory.decodeByteArray(pArray, 0, nWidth * nHeight * nPlane);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            m_pImage = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ALPHA_8);
            return false;
        }
    }

    public boolean fromIntegerArray(int[] pArray, int nWidth, int nHeight) {
        if (pArray.length != nWidth * nHeight) throw new IndexOutOfBoundsException();
        try {
            m_pImage = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ARGB_8888);
            m_pImage.setPixels(pArray, 0, nWidth, 0, 0, nWidth, nHeight);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            m_pImage = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ALPHA_8);
            return false;
        }
    }
}