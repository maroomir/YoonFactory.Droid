package com.yoonfactory.image;

import java.io.IOException;

public class TwoImageProcess {

    public static YoonImage combine(YoonImage pSourceImage, YoonImage pObjectImage) {
        if (pSourceImage.getWidth() != pObjectImage.getWidth() || pSourceImage.getHeight() != pObjectImage.getHeight())
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Source and object size is not same");
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(combine(pSourceImage.toByteArray(), pObjectImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] combine(byte[] pSourceBuffer, byte[] pObjectBuffer, int width, int height) {
        int i, j;
        byte[] pResultBuffer;
        pResultBuffer = new byte[width * height];
        for (j = 0; j < height; j++) {
            for (i = 0; i < width; i++) {
                if (pSourceBuffer[j * width + i] > pObjectBuffer[j * width + i])
                    pResultBuffer[j * width + i] = pSourceBuffer[j * width + i];
                else
                    pResultBuffer[j * width + i] = pObjectBuffer[j * width + i];
            }
        }
        return pResultBuffer;
    }

    public static YoonImage add(YoonImage pSourceImage, YoonImage pObjectImage) {
        if (pSourceImage.getWidth() != pObjectImage.getWidth() || pSourceImage.getHeight() != pObjectImage.getHeight())
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Source and object size is not same");
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(add(pSourceImage.toByteArray(), pObjectImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] add(byte[] pSourceBuffer, byte[] pObjectBuffer, int width, int height) {
        int i, j, value;
        int maxValue, minValue;
        int[] pTempBuffer;
        byte[] pResultBuffer;
        double ratio;
        maxValue = 0;
        minValue = 1024;
        pTempBuffer = new int[width * height];
        pResultBuffer = new byte[width * height];
        for (j = 0; j < height; j++) {
            for (i = 0; i < width; i++) {
                pTempBuffer[j * width + i] = pSourceBuffer[j * width + i] + pObjectBuffer[j * width + i];
            }
        }
        ////  합해진 Buffer(pBuffer)의 최대 Gray Level 값과 최소 Gray Level 값을 산출함.
        for (j = 0; j < height; j++) {
            for (i = 0; i < width; i++) {
                value = pTempBuffer[j * width + i];
                if (value < minValue) minValue = value;
                if (value > maxValue) maxValue = value;
            }
        }
        ////  최대 Gray Level값이 255를 넘는 경우, 이에 맞게 Image 전체 Gray Level을 조정함.
        if (maxValue > 255) {
            ratio = 255.0 / (double) maxValue;
            for (j = 0; j < height; j++) {
                for (i = 0; i < width; i++) {
                    value = (int) (pTempBuffer[j * width + i] * ratio);
                    if (value > 255) value = 255;
                    if (value < 0) value = 0;
                    pResultBuffer[j * width + i] = (byte) value;
                }
            }
        }
        return pResultBuffer;
    }

    public static YoonImage subtract(YoonImage pSourceImage, YoonImage pObjectImage) {
        if (pSourceImage.getWidth() != pObjectImage.getWidth() || pSourceImage.getHeight() != pObjectImage.getHeight())
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Source and object size is not same");
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(subtract(pSourceImage.toByteArray(), pObjectImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] subtract(byte[] pSourceBuffer, byte[] pObjectBuffer, int width, int height) {
        int i, j, value;
        byte[] pResultBuffer;
        pResultBuffer = new byte[width * height];
        for (j = 0; j < height; j++) {
            for (i = 0; i < width; i++) {
                value = pSourceBuffer[j * width + i] - pObjectBuffer[j * width + i];
                if (value > 255) value = 255;
                if (value < 0) value = 0;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        return pResultBuffer;
    }
}
