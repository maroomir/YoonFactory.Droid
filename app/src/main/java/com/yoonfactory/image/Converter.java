package com.yoonfactory.image;

import com.yoonfactory.BitConverter;

public class Converter {
    public static byte[] to8BitGrayBuffer(int[] pBuffer, int nWidth, int nHeight) {
        if (pBuffer.length != nWidth * nHeight) return null;

        byte[] pByte = new byte[pBuffer.length];
        for (int j = 0; j < nHeight; j++) {
            for (int i = 0; i < nWidth; i++) {
                byte[] pBytePixel = BitConverter.getBytes(pBuffer[j * nWidth + i]); // Order by {B/G/R/A}
                pByte[j * nWidth + i] = (byte) (0.299f * pBytePixel[2] + 0.587f * pBytePixel[1] + 0.114f * pBytePixel[0]); // ITU-RBT.709, YPrPb
            }
        }
        return pByte;
    }

    public static int[] to24BitColorBuffer(byte[] pRed, byte[] pGreen, byte[] pBlue, int nWidth, int nHeight) throws Exception {
        if (pRed == null || pRed.length != nWidth * nHeight ||
                pGreen == null || pGreen.length != nWidth * nHeight ||
                pBlue == null || pBlue.length != nWidth * nHeight)
            return null;

        int[] pPixel = new int[nWidth * nHeight];
        for (int j = 0; j < nHeight; j++) {
            for (int i = 0; i < nWidth; i++) {
                byte[] pBytePixel = new byte[4];
                pBytePixel[0] = pBlue[j * nWidth + i];
                pBytePixel[1] = pGreen[j * nWidth + i];
                pBytePixel[2] = pRed[j * nWidth + i];
                pBytePixel[3] = (byte) 0;
                pPixel[i] = BitConverter.toInt32(pBytePixel, 0);
            }
        }
        return pPixel;
    }

    public static byte[] to8BitRedBuffer(int[] pBuffer, int nWidth, int nHeight) {
        if (pBuffer.length != nWidth * nHeight) return null;

        byte[] pByte = new byte[pBuffer.length];
        for (int j = 0; j < nHeight; j++) {
            for (int i = 0; i < nWidth; i++) {
                byte[] pBytePixel = BitConverter.getBytes(pBuffer[j * nWidth + i]); // Order by {B/G/R/A}
                pByte[j * nWidth] = pBytePixel[2];
            }
        }
        return pByte;
    }

    public static byte[] to8BitGreenBuffer(int[] pBuffer, int nWidth, int nHeight) {
        if (pBuffer.length != nWidth * nHeight) return null;

        byte[] pByte = new byte[pBuffer.length];
        for (int j = 0; j < nHeight; j++) {
            for (int i = 0; i < nWidth; i++) {
                byte[] pBytePixel = BitConverter.getBytes(pBuffer[j * nWidth + i]); // Order by {B/G/R/A}
                pByte[j * nWidth] = pBytePixel[1];
            }
        }
        return pByte;
    }

    public static byte[] to8BitBlueBuffer(int[] pBuffer, int nWidth, int nHeight) {
        if (pBuffer.length != nWidth * nHeight) return null;

        byte[] pByte = new byte[pBuffer.length];
        for (int j = 0; j < nHeight; j++) {
            for (int i = 0; i < nWidth; i++) {
                byte[] pBytePixel = BitConverter.getBytes(pBuffer[j * nWidth + i]); // Order by {B/G/R/A}
                pByte[j * nWidth] = pBytePixel[0];
            }
        }
        return pByte;
    }
}