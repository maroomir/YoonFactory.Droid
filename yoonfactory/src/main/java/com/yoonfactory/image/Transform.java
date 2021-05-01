package com.yoonfactory.image;

import com.yoonfactory.YoonVector2N;

import java.io.IOException;

public class Transform {

    //  Image 확대, 축소하기.
    public static YoonImage zoom(YoonImage pSourceImage, double dRatio) throws IOException {
        if (pSourceImage.getPlane() == 1)
            return new YoonImage(zoom(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), dRatio),
                    pSourceImage.getWidth(), pSourceImage.getHeight());
        else if (pSourceImage.getPlane() == 4)
            return new YoonImage(zoom(pSourceImage.toIntegerArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), dRatio),
                    pSourceImage.getWidth(), pSourceImage.getHeight());
        else
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
    }

    public static YoonImage zoom(YoonImage pSourceImage, double dRatioX, double dRatioY) throws IOException {
        if (pSourceImage.getPlane() == 1)
            return new YoonImage(zoom(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), dRatioX, dRatioY),
                    pSourceImage.getWidth(), pSourceImage.getHeight());
        else if (pSourceImage.getPlane() == 4)
            return new YoonImage(zoom(pSourceImage.toIntegerArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), dRatioX, dRatioY),
                    pSourceImage.getWidth(), pSourceImage.getHeight());
        else
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
    }

    public static byte[] zoom(byte[] pBuffer, int sourceWidth, int sourceHeight, double dRatio) {
        return zoom(pBuffer, sourceWidth, sourceHeight, (int) (sourceWidth * dRatio), (int) (sourceHeight * dRatio));
    }

    public static byte[] zoom(byte[] pBuffer, int sourceWidth, int sourceHeight, double dRatioX, double dRatioY) {
        return zoom(pBuffer, sourceWidth, sourceHeight, (int) (sourceWidth * dRatioX), (int) (sourceHeight * dRatioY));
    }

    public static byte[] zoom(byte[] pBuffer, int sourceWidth, int sourceHeight, int destWidth, int destHeight) {
        int i, j, x, y;
        float x1, y1, x2, y2;                               // 좌표
        float ratioX, ratioY;
        byte intensity1, intensity2, intensity3, intensity4; // Gray Level
        float value1, value2, resultLevel;
        ratioX = (float) destWidth / (float) (sourceWidth - 1);
        ratioY = (float) destHeight / (float) (sourceHeight - 1);
        byte[] pDestination = new byte[destWidth * destHeight];
        ////  비율별 확대, 축소
        for (j = 1; j < sourceHeight; j++) {
            for (i = 1; i < sourceWidth; i++) {
                x1 = (float) (i - 1) * ratioX;
                y1 = (float) (j - 1) * ratioY;
                x2 = (float) i * ratioX;
                y2 = (float) j * ratioY;

                if (x2 >= destWidth) x2 = destWidth - 1;
                if (y2 >= destHeight) y2 = destHeight - 1;
                intensity1 = pBuffer[(j - 1) * sourceWidth + (i - 1)];
                intensity2 = pBuffer[(j - 1) * sourceWidth + i];
                intensity3 = pBuffer[j * sourceWidth + (i - 1)];
                intensity4 = pBuffer[j * sourceWidth + i];
                for (y = (int) y1; y <= (int) y2; y++) {
                    for (x = (int) x1; x <= (int) x2; x++) {
                        value1 = (x - x2) * intensity1 / (x1 - x2) + (x - x1) * intensity2 / (x2 - x1);
                        value2 = (x - x2) * intensity3 / (x1 - x2) + (x - x1) * intensity4 / (x2 - x1);
                        resultLevel = (y - y2) * value1 / (y1 - y2) + (y - y1) * value2 / (y2 - y1);
                        pDestination[y * destWidth + x] = (byte) resultLevel;
                    }
                }
            }
        }
        return pDestination;
    }

    public static int[] zoom(int[] pBuffer, int sourceWidth, int sourceHeight, double dRatio) {
        return zoom(pBuffer, sourceWidth, sourceHeight, (int) (sourceWidth * dRatio), (int) (sourceHeight * dRatio));
    }

    public static int[] zoom(int[] pBuffer, int sourceWidth, int sourceHeight, double dRatioX, double dRatioY) {
        return zoom(pBuffer, sourceWidth, sourceHeight, (int) (sourceWidth * dRatioX), (int) (sourceHeight * dRatioY));
    }

    public static int[] zoom(int[] pBuffer, int sourceWidth, int sourceHeight, int destWidth, int destHeight) {
        int i, j, x, y;
        float x1, y1, x2, y2;                               // 좌표
        float ratioX, ratioY;
        int intensity1, intensity2, intensity3, intensity4; // Gray Level
        float value1, value2, resultLevel;
        ratioX = (float) destWidth / (float) (sourceWidth - 1);
        ratioY = (float) destHeight / (float) (sourceHeight - 1);
        int[] pDestination = new int[destWidth * destHeight];
        ////  비율별 확대, 축소
        for (j = 1; j < sourceHeight; j++) {
            for (i = 1; i < sourceWidth; i++) {
                x1 = (float) (i - 1) * ratioX;
                y1 = (float) (j - 1) * ratioY;
                x2 = (float) i * ratioX;
                y2 = (float) j * ratioY;

                if (x2 >= destWidth) x2 = destWidth - 1;
                if (y2 >= destHeight) y2 = destHeight - 1;
                intensity1 = pBuffer[(j - 1) * sourceWidth + (i - 1)];
                intensity2 = pBuffer[(j - 1) * sourceWidth + i];
                intensity3 = pBuffer[j * sourceWidth + (i - 1)];
                intensity4 = pBuffer[j * sourceWidth + i];
                for (y = (int) y1; y <= (int) y2; y++) {
                    for (x = (int) x1; x <= (int) x2; x++) {
                        value1 = (x - x2) * intensity1 / (x1 - x2) + (x - x1) * intensity2 / (x2 - x1);
                        value2 = (x - x2) * intensity3 / (x1 - x2) + (x - x1) * intensity4 / (x2 - x1);
                        resultLevel = (y - y2) * value1 / (y1 - y2) + (y - y1) * value2 / (y2 - y1);
                        pDestination[y * destWidth + x] = (int) resultLevel;
                    }
                }
            }
        }
        return pDestination;
    }

    //  회전.
    public static YoonImage rotate(YoonImage pSourceImage, YoonVector2N vecCenter, double dAngle) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(rotate(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), vecCenter.getX(), vecCenter.getY(), dAngle),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] rotate(byte[] pBuffer, int bufferWidth, int bufferHeight, int centerX, int centerY, double angle) {
        int i, j;
        int x1, y1, x2, y2;
        double theta, sinTheta, cosTheta;
        double posX1, posY1, posX2, posY2;
        double level1, level2, level3, level4, tempLevel;
        double roundX, roundY;
        if (Math.abs(angle) < 0.001)
            angle = 0.001;
        theta = angle * 3.141592 / 180.0;
        sinTheta = Math.sin(theta);
        cosTheta = Math.cos(theta);
        byte[] pResultBuffer = new byte[bufferWidth * bufferHeight];
        ////  회전 알고리즘.
        for (j = 0; j < bufferHeight; j++) {
            posY1 = (double) j - (double) centerY;
            for (i = 0; i < bufferWidth; i++) {
                //////  Image 선회전.
                posX1 = (double) i - (double) centerX;
                posX2 = (double) centerX + (posX1 * cosTheta - posY1 * sinTheta);
                posY2 = (double) centerY + (posX1 * sinTheta + posY1 * cosTheta);
                if (posX2 < 0.0 || posY2 < 0.0)
                    continue;
                //////  Image 보간.
                x1 = (int) posX2;  // 정수형으로 변환하여 위치좌표 얻는다.
                y1 = (int) posY2;
                x2 = x1 + 1;
                y2 = y1 + 1;
                //////  좌표가 한계 범위를 넘은 경우 스킵.
                if (x1 < 0 || x1 >= bufferWidth || y1 < 0 || y2 >= bufferHeight)
                    continue;
                roundX = posX2 - x1;
                roundY = posY2 - y1;
                //////  보간을 위한 4개 좌표의 Pixel 산출 후 Filtering으로 Gray Level 선정.
                level1 = pBuffer[y1 * bufferWidth + x1];
                level2 = pBuffer[y1 * bufferWidth + x2];
                level3 = pBuffer[y2 * bufferWidth + x1];
                level4 = pBuffer[y2 * bufferWidth + x2];
                tempLevel = (1.0 - roundX) * (1.0 - roundY) * level1 + roundX * (1.0 - roundY) * level2 + (1.0 - roundX) * roundY * level3 + roundX * roundY * level4;
                if (tempLevel < 0) tempLevel = 0;
                if (tempLevel > 255) tempLevel = 255;
                pResultBuffer[j * bufferWidth + i] = (byte) tempLevel;
            }
        }
        return pResultBuffer;
    }

    //  Image 반전.
    public static YoonImage reverse(YoonImage pSourceImage) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(reverse(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] reverse(byte[] pBuffer, int bufferWidth, int bufferHeight) {
        int i, j;
        byte[] pResultBuffer = new byte[bufferWidth * bufferHeight];
        for (j = 0; j < bufferHeight; j++) {
            for (i = 0; i < bufferWidth; i++) {
                pResultBuffer[j * bufferWidth + i] = (byte) (255 - pBuffer[j * bufferWidth + i]);
            }
        }
        return pResultBuffer;
    }
}