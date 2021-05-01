package com.yoonfactory.image;

import com.yoonfactory.YoonRect2N;

public class Morphology {

    //  침식 연산.
    public static YoonImage erosion(YoonImage pSourceImage) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(erosion(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonImage erosion(YoonImage pSourceImage, YoonRect2N scanArea) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(erosion(pSourceImage.toByteArray(), pSourceImage.getWidth(), scanArea),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] erosion(byte[] pBuffer, int bufferWidth, int bufferHeight) {
        int i, j, x, y;
        int posX, posY;
        int value, minValue;
        byte[] pResultBuffer = pBuffer.clone();
        ////  침식 연산용 Masking.
        for (y = 0; y < bufferHeight - 2; y++) {
            for (x = 0; x < bufferWidth - 2; x++) {
                minValue = 100000;
                //////  주변의 아홉개 IYoonVector 中 최소 Gray Level 산출.
                for (j = 0; j < 3; j++) {
                    for (i = 0; i < 3; i++) {
                        posX = x + i;
                        posY = y + j;
                        value = pBuffer[posY * bufferWidth + posX];
                        if (value < minValue)
                            minValue = value;
                    }
                }
                //////  다음 IYoonVector에 해당 Gray Level 대입.
                posX = x + 1;
                posY = y + 1;
                pResultBuffer[posY * bufferWidth + posX] = (byte) minValue;
            }
        }
        return pResultBuffer;
    }

    public static byte[] erosion(byte[] pBuffer, int bufferWidth, YoonRect2N scanArea) {
        int i, j, x, y;
        int posX, posY;
        int value, minValue;
        int scanWidth = scanArea.Width;
        int scanHeight = scanArea.Height;
        byte[] pResultBuffer = pBuffer.clone();
        byte[] pTempBuffer = new byte[scanWidth * scanHeight];
        for (j = 0; j < scanHeight; j++)
            for (i = 0; i < scanWidth; i++)
                pTempBuffer[j * scanWidth + i] = pBuffer[(scanArea.getTop() + j) * bufferWidth + scanArea.getLeft() + i];
        ////  침식 연산용 Masking.
        for (y = 0; y < scanHeight - 2; y++) {
            for (x = 0; x < scanWidth - 2; x++) {
                minValue = 100000;
                //////  주변의 아홉개 IYoonVector 中 최소 Gray Level 산출.
                for (j = 0; j < 3; j++) {
                    for (i = 0; i < 3; i++) {
                        posX = x + i;
                        posY = y + j;
                        value = pTempBuffer[posY * bufferWidth + posX];
                        if (value < minValue)
                            minValue = value;
                    }
                }
                //////  다음 IYoonVector에 해당 Gray Level 대입.
                posX = scanArea.getLeft() + x + 1;
                posY = scanArea.getTop() + y + 1;
                pResultBuffer[posY * bufferWidth + posX] = (byte) minValue;
            }
        }
        return pResultBuffer;
    }

    public static YoonImage erosionAsBinary(YoonImage pSourceImage) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(erosionAsBinary(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonImage erosionAsBinary(YoonImage pSourceImage, int nMophSize) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(erosionAsBinary(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nMophSize),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonImage erosionAsBinary(YoonImage pSourceImage, YoonRect2N scanArea) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(erosionAsBinary(pSourceImage.toByteArray(), pSourceImage.getWidth(), scanArea),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] erosionAsBinary(byte[] pBuffer, int bufferWidth, int bufferHeight) {
        int i, j, x, y;
        int posX, posY;
        int sum;
        byte[] pResultBuffer = pBuffer.clone();
        byte[][] mask = {{(byte) 255, (byte) 255, (byte) 255},
                {(byte) 255, (byte) 255, (byte) 255},
                {(byte) 255, (byte) 255, (byte) 255}};
        for (y = 0; y < bufferHeight - 2; y++) {
            for (x = 0; x < bufferWidth - 2; x++) {
                sum = 0;
                //////  주변이 모두 흰색일 경우에만 흰색(255)으로 표시할 수 있음.
                for (i = 0; i < 3; i++) {
                    for (j = 0; j < 3; j++) {
                        posX = x + i;
                        posY = y + j;
                        if (pBuffer[posY * bufferWidth + posX] == mask[i][j])
                            sum++;
                    }
                }
                //////  다음 IYoonVector에 침식 Gray Level 결과 대입.
                posX = x + 1;
                posY = y + 1;
                if (sum == 9)
                    pResultBuffer[posY * bufferWidth + posX] = (byte) 255;
                else
                    pResultBuffer[posY * bufferWidth + posX] = 0;
            }
        }
        return pResultBuffer;
    }

    //  size 조정 가능한 침식 연산.
    public static byte[] erosionAsBinary(byte[] pBuffer, int bufferWidth, int bufferHeight, int size) {
        boolean isBlack;
        int i, j, x, y;
        int posX, posY;
        byte[] pResultBuffer = pBuffer.clone();
        //// 침식 연산 Masking 작업.
        for (y = 0; y < bufferHeight - size; y++) {
            for (x = 0; x < bufferWidth - size; x++) {
                isBlack = false;
                //////  가상 Masking과 비교.
                for (i = 0; i < size; i++) {
                    for (j = 0; j < size; j++) {
                        //////  주변의 Pixel들中 하나라도 검은색이면 검은색(0)임.
                        posX = x + i;
                        posY = y + j;
                        if (pBuffer[(y + j) * bufferWidth + (x + i)] == 0) {
                            isBlack = true;
                            break;
                        }
                    }
                }
                //////  다음 IYoonVector에 침식 판단 결과 대입.
                posX = x + size / 2;
                posY = y + size / 2;
                if (isBlack)
                    pResultBuffer[posY * bufferWidth + posX] = 0;
                else
                    pResultBuffer[posY * bufferWidth + posX] = (byte) 255;
            }
        }
        return pResultBuffer;
    }

    public static byte[] erosionAsBinary(byte[] pBuffer, int bufferWidth, YoonRect2N scanArea) {
        int x, y, i, j;
        int posX, posY;
        int sum;
        int scanWidth = scanArea.Width;
        int scanHeight = scanArea.Height;
        byte[] pResultBuffer = pBuffer.clone();
        byte[] pTempBuffer = new byte[scanWidth * scanHeight];
        byte[][] mask = {{(byte) 255, (byte) 255, (byte) 255},
                {(byte) 255, (byte) 255, (byte) 255},
                {(byte) 255, (byte) 255, (byte) 255}};
        for (j = 0; j < scanHeight; j++)
            for (i = 0; i < scanWidth; i++)
                pTempBuffer[j * scanWidth + i] = pBuffer[(scanArea.getTop() + j) * bufferWidth + scanArea.getLeft() + i];
        ////  침식 연산 Masking 작업.
        for (y = 0; y < scanHeight - 2; y++) {
            for (x = 0; x < scanWidth - 2; x++) {
                sum = 0;
                //////  주변이 모두 흰색일 경우에만 흰색(255)으로 표시할 수 있음.
                for (i = 0; i < 3; i++) {
                    for (j = 0; j < 3; j++) {
                        posX = x + i;
                        posY = y + j;
                        if (pTempBuffer[posY * scanWidth + posX] == mask[i][j])
                            sum++;
                    }
                }
                //////  다음 IYoonVector에 침식 Gray Level 결과 대입.
                posX = scanArea.getLeft() + x + 1;
                posY = scanArea.getTop() + y + 1;
                if (sum == 9) {
                    pResultBuffer[posY * bufferWidth + posX] = (byte) 255;
                } else {
                    pResultBuffer[posY * bufferWidth + posX] = 0;
                }
            }
        }
        return pResultBuffer;
    }

    //  팽장 연산.
    public static YoonImage dilation(YoonImage pSourceImage) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(dilation(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonImage dilation(YoonImage pSourceImage, YoonRect2N scanArea) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(dilation(pSourceImage.toByteArray(), pSourceImage.getWidth(), scanArea),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] dilation(byte[] pBuffer, int bufferWidth, int bufferHeight) {
        int i, j, x, y;
        int posX, posY;
        int value, maxValue;
        byte[] pResultBuffer = pBuffer.clone();
        ////  팽창 연산 Masking 작업.
        for (y = 0; y < bufferHeight - 2; y++) {
            for (x = 0; x < bufferWidth - 2; x++) {
                maxValue = 0;
                //////  주변의 아홉개 IYoonVector 中 최대 Gray Level 산출.
                for (j = 0; j < 3; j++) {
                    for (i = 0; i < 3; i++) {
                        posX = x + i;
                        posY = y + j;
                        value = pBuffer[posY * bufferWidth + posX];
                        if (value > maxValue)
                            maxValue = value;
                    }
                }
                //////  다음 IYoonVector에 해당 Gray Level 대입.
                posX = x + 1;
                posY = y + 1;
                pBuffer[posY * bufferWidth + posX] = (byte) maxValue;
            }
        }
        return pResultBuffer;
    }

    public static byte[] dilation(byte[] pBuffer, int bufferWidth, YoonRect2N scanArea) {
        int i, j, x, y;
        int posX, posY;
        int value, maxValue;
        int scanWidth = scanArea.Width;
        int scanHeight = scanArea.Height;
        byte[] pResultBuffer = pBuffer.clone();
        byte[] pTempBuffer = new byte[scanWidth * scanHeight];
        for (j = 0; j < scanHeight; j++)
            for (i = 0; i < scanWidth; i++)
                pTempBuffer[j * scanWidth + i] = pBuffer[(scanArea.getTop() + j) * bufferWidth + scanArea.getLeft() + i];
        ////  침식 연산용 Masking
        for (y = 0; y < scanHeight - 2; y++) {
            for (x = 0; x < scanWidth - 2; x++) {
                maxValue = 0;
                //////  주변의 아홉개 IYoonVector 中 최대 Gray Level 산출.
                for (j = 0; j < 3; j++) {
                    for (i = 0; i < 3; i++) {
                        posX = x + i;
                        posY = y + j;
                        value = pBuffer[posY * bufferWidth + posX];
                        if (value > maxValue)
                            maxValue = value;
                    }
                }
                //////  다음 IYoonVector에 해당 Gray Level 대입.
                posX = scanArea.getLeft() + x + 1;
                posY = scanArea.getTop() + y + 1;
                pBuffer[posY * bufferWidth + posX] = (byte) maxValue;
            }
        }
        return pResultBuffer;
    }

    public static YoonImage dilationAsBinary(YoonImage pSourceImage) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(dilationAsBinary(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonImage dilationAsBinary(YoonImage pSourceImage, int nMophSize) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(dilationAsBinary(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nMophSize),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonImage dilationAsBinary(YoonImage pSourceImage, YoonRect2N scanArea) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(dilationAsBinary(pSourceImage.toByteArray(), pSourceImage.getWidth(), scanArea),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonImage dilationAsBinary(YoonImage pSourceImage, YoonRect2N scanArea, int nMophSize) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format is not correct");
        return new YoonImage(dilationAsBinary(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), scanArea, nMophSize),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] dilationAsBinary(byte[] pBuffer, int bufferWidth, int bufferHeight) {
        int i, j, x, y;
        int posX, posY;
        boolean isWhite;
        byte[] pResultBuffer = pBuffer.clone();
        for (y = 0; y < bufferHeight - 2; y++) {
            for (x = 0; x < bufferWidth - 2; x++) {
                isWhite = false;
                for (i = 0; i < 3; i++) {
                    for (j = 0; j < 3; j++) {
                        posX = x + i;
                        posY = y + j;
                        if (pBuffer[posY * bufferWidth + posX] > 0) {
                            isWhite = true;
                            break;
                        }
                    }
                    if (isWhite) break;
                }
                posX = x + 1;
                posY = y + 1;
                if (isWhite)
                    pResultBuffer[posY * bufferWidth + posX] = (byte) 255;
                else
                    pResultBuffer[posY * bufferWidth + posX] = 0;
            }
        }
        return pResultBuffer;
    }

    public static byte[] dilationAsBinary(byte[] pBuffer, int bufferWidth, YoonRect2N scanArea) {
        int i, j, x, y;
        int posX, posY;
        boolean isWhite;
        int scanWidth = scanArea.Width;
        int scanHeight = scanArea.Height;
        byte[] pResultBuffer = pBuffer.clone();
        byte[] pTempBuffer = new byte[scanWidth * scanHeight];
        for (j = 0; j < scanHeight; j++)
            for (i = 0; i < scanWidth; i++)
                pTempBuffer[j * scanWidth + i] = pBuffer[(scanArea.getTop() + j) * bufferWidth + (scanArea.getLeft() + i)];
        ////  팽창 연산 Masking 작업.
        for (y = 0; y < scanHeight - 2; y++) {
            for (x = 0; x < scanWidth - 2; x++) {
                isWhite = false;
                for (i = 0; i < 3; i++) {
                    for (j = 0; j < 3; j++) {
                        posX = x + i;
                        posY = y + j;
                        if (pTempBuffer[posY * scanWidth + posX] > 0) {
                            isWhite = true;
                            break;
                        }
                    }
                    if (isWhite) break;
                }
                posX = scanArea.getLeft() + (x + 1);
                posY = scanArea.getTop() + (y + 1);
                if (isWhite)
                    pResultBuffer[posY * bufferWidth + posX] = (byte) 255;
                else
                    pResultBuffer[posY * bufferWidth + posX] = 0;
            }
        }
        return pResultBuffer;
    }

    //  size 조정 가능한 팽창 연산.
    public static byte[] dilationAsBinary(byte[] pBuffer, int bufferWidth, int bufferHeight, int size) {
        int i, j, x, y;
        int posX, posY;
        boolean isWhite;
        byte[] pResultBuffer = pBuffer.clone();
        ////  Buffer 영역 팽창 연산 Masking 작업.
        for (y = 0; y < bufferHeight - size; y++) {
            for (x = 0; x < bufferWidth - size; x++) {
                isWhite = false;
                for (i = 0; i < size; i++) {
                    for (j = 0; j < size; j++) {
                        posX = x + i;
                        posY = y + j;
                        if (pResultBuffer[posY * bufferWidth + posX] > 0) {
                            isWhite = true;
                            break;
                        }
                    }
                    if (isWhite) break;
                }
                posX = x + size / 2;
                posY = y + size / 2;
                if (isWhite)
                    pResultBuffer[posY * bufferWidth + posX] = (byte) 255;
                else
                    pResultBuffer[posY * bufferWidth + posX] = 0;
            }
        }
        return pResultBuffer;
    }

    //  Filter 테두리 형태로 팽창 검사.
    public static byte[] dilationAsBinary(byte[] pBuffer, int bufferWidth, int bufferHeight, YoonRect2N scanArea, int size) {
        int i, j, i1, j1, i2, j2;
        int x, y;
        int posX, posY;
        boolean isWhite;
        byte[] pResultBuffer = pBuffer.clone();
        ////  테두리 부분(0, size-1)만 따로 검사.
        for (y = scanArea.getTop(); y < scanArea.getBottom() - size; y++) {
            for (x = scanArea.getLeft(); x < scanArea.getRight() - size; x++) {
                isWhite = false;
                j1 = 0;
                j2 = size - 1;
                for (i = 0; i < size; i++) {
                    if (pBuffer[(y + j1) * bufferWidth + (x + i)] > 0 || pBuffer[(y + j2) * bufferWidth + (x + i)] > 0) {
                        isWhite = true;
                        break;
                    }
                }
                i1 = 0;
                i2 = size - 1;
                for (j = 0; j < size; j++) {
                    if (isWhite) break;
                    if (pBuffer[(y + j) * bufferWidth + (x + i1)] > 0 || pBuffer[(y + j) * bufferWidth + (x + i2)] > 0) {
                        isWhite = true;
                        break;
                    }
                }
                posX = x + size / 2;
                posY = y + size / 2;
                if (isWhite)
                    pResultBuffer[posY * bufferWidth + posX] = (byte) 255;
                else
                    pResultBuffer[posY * bufferWidth + posX] = 0;
            }
        }
        return pResultBuffer;
    }
}