package com.yoonfactory.image;

import com.yoonfactory.YoonRect2N;

public class Binary {
    public static YoonImage binarize(YoonImage pSourceImage, YoonRect2N scanArea, byte nThreshold) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(binarize(pSourceImage.toByteArray(), pSourceImage.getWidth(), scanArea, nThreshold), pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] binarize(byte[] pBuffer, int bufferWidth, YoonRect2N scanArea, byte threshold) {
        byte[] pResultBuffer = new byte[pBuffer.length];
        for (int j = scanArea.getTop(); j < scanArea.getBottom(); j++) {
            for (int i = scanArea.getLeft(); i < scanArea.getRight(); i++) {
                if (pBuffer[j * bufferWidth + i] < threshold)
                    pResultBuffer[j * bufferWidth + i] = 0;
                else
                    pResultBuffer[j * bufferWidth + i] = (byte) 255;
            }
        }
        return pResultBuffer;
    }

    public static YoonImage binarize(YoonImage pSourceImage, byte nThreshold) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(binarize(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nThreshold), pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] binarize(byte[] pBuffer, int bufferWidth, int bufferHeight, byte threshold) {
        int sum = 0;
        int count = 0;
        byte tempThreshold = threshold;
        byte[] pResultBuffer = new byte[bufferWidth * bufferHeight];
        ////  threshold 값 보정.
        if (threshold < 1) {
            for (int j = 0; j < bufferHeight; j++) {
                for (int i = 0; i < bufferWidth; i++) {
                    sum += pBuffer[j * bufferWidth + i];
                    count++;
                }
            }
            if (count < 1)
                tempThreshold = (byte) 255;
            else
                tempThreshold = (byte) (sum / count + 20);
        }
        ////  이진화.
        for (int j = 0; j < bufferHeight; j++) {
            for (int i = 0; i < bufferWidth; i++) {
                if (pBuffer[j * bufferWidth + i] < tempThreshold)
                    pResultBuffer[j * bufferWidth + i] = 0;
                else
                    pResultBuffer[j * bufferWidth + i] = (byte) 255;
            }
        }
        return pResultBuffer;
    }
}