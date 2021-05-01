package com.yoonfactory.image;

import com.yoonfactory.MathFactory;
import com.yoonfactory.YoonRect2N;

import java.io.IOException;

public class PatternMatch {
    public static YoonObject findPatternAsBinary(YoonImage pPatternImage, YoonImage pSourceImage) throws IOException {
        if (pPatternImage.getPlane() != 1 || pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return PatternMatch.findPatternAsBinary(pPatternImage.toByteArray(), pPatternImage.getWidth(), pPatternImage.getHeight(), pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), true);
    }

    public static YoonObject findPatternAsBinary(byte[] pPatternBuffer, int patternWidth, int patternHeight, byte[] pSourceBuffer, int sourceWidth, int sourceHeight, boolean bWhite) {
        int findPosX, findPosY;
        int startX, startY, jumpX, jumpY;
        int graySource, grayPattern;
        int whiteCount, totalCountWhite;
        int blackCount, totalCountBlack;
        int whiteCountMax, blackCountMax;
        double dCoefficient = 0.0;
        ////  초기화
        YoonRect2N findRect = new YoonRect2N(0, 0, 0, 0);
        ////  Skip 정도 지정
        jumpX = patternWidth / 30;
        jumpY = patternHeight / 30;
        if (jumpX < 1) jumpX = 1;
        if (jumpY < 1) jumpY = 1;
        findPosX = 0;
        findPosY = 0;
        ////  Match 갯수 및 정도를 찾는다.
        whiteCountMax = 0;
        blackCountMax = 0;
        for (int iY = 0; iY < sourceHeight - patternHeight; iY += 1) {
            for (int iX = 0; iX < sourceWidth - patternWidth; iX += 1) {
                startX = iX;
                startY = iY;
                ////// 전체 영역 내에서의 차이 값을 구한다.
                whiteCount = 0;
                blackCount = 0;
                totalCountWhite = 0;
                totalCountBlack = 0;
                for (int y = 0; y < patternHeight - jumpY; y += jumpY) {
                    for (int x = 0; x < patternWidth - jumpX; x += jumpX) {
                        graySource = pSourceBuffer[(startY + y) * sourceWidth + startX + x];
                        grayPattern = pPatternBuffer[y * patternWidth + x];
                        //////  Pattern과 Source의 Gray Level이 같은 경우 match Count를 늘린다.
                        if (grayPattern == 0) {
                            totalCountBlack++;
                            if (grayPattern == graySource)
                                blackCount++;
                        }
                        if (grayPattern == 255) {
                            totalCountWhite++;
                            if (grayPattern == graySource)
                                whiteCount++;
                        }
                    }
                }
                ////// 최대한 White IYoonVector가 많은 Pattern을 찾는다.
                if (bWhite) {
                    if (whiteCount > whiteCountMax) {
                        whiteCountMax = whiteCount;
                        findPosX = iX;
                        findPosY = iY;
                        dCoefficient = 0.0;
                        if (totalCountWhite > 1)
                            dCoefficient = whiteCount * 100.0 / totalCountWhite;
                    }
                } else {
                    if (blackCount > blackCountMax) {
                        blackCountMax = blackCount;
                        findPosX = iX;
                        findPosY = iY;
                        dCoefficient = 0.0;
                        if (totalCountBlack > 1)
                            dCoefficient = blackCount * 100.0 / totalCountBlack;
                    }
                }
            }
        }
        findRect.CenterPos.setX(findPosX);
        findRect.CenterPos.setY(findPosY);
        findRect.Width = patternWidth;
        findRect.Height = patternHeight;
        return new YoonObject<YoonRect2N>(0, findRect, dCoefficient, (bWhite) ? whiteCountMax : blackCountMax);
    }

    public static YoonObject findPatternAsBinary(YoonRect2N scanArea, YoonImage pPatternImage, YoonImage pSourceImage) throws IOException {
        if (pPatternImage.getPlane() != 1 || pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        if (!pPatternImage.isVerifiedArea(scanArea))
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Scan area is not verified");
        return findPatternAsBinary(scanArea, pPatternImage.toByteArray(), pPatternImage.getWidth(), pPatternImage.getHeight(), pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static YoonObject findPatternAsBinary(YoonRect2N scanArea, byte[] pPatternBuffer, int patternWidth, int patternHeight, byte[] pSourceBuffer, int sourceWidth, int sourceHeight) {
        int findPosX, findPosY;
        int startX, startY, jumpX, jumpY;
        int graySource, grayPattern;
        int whiteCount, totalCountWhite;
        int blackCount, totalCountBlack;
        int matchCount, matchCountMax;
        double dCoefficient = 0.0;
        ////  초기화
        YoonRect2N findRect = new YoonRect2N(0, 0, 0, 0);
        ////  Skip 정도 지정
        jumpX = patternWidth / 30;
        jumpY = patternHeight / 30;
        if (jumpX < 1) jumpX = 1;
        if (jumpY < 1) jumpY = 1;
        findPosX = 0;
        findPosY = 0;
        ////  Match 갯수 및 정도를 찾는다.
        matchCountMax = 0;
        for (int iY = scanArea.getTop(); iY < scanArea.getBottom() - patternHeight; iY += 1) {
            for (int iX = scanArea.getLeft(); iX < scanArea.getRight() - patternWidth; iX += 1) {
                startX = iX;
                startY = iY;
                ////// Scan Area 영역 내에서의 차이 값을 구한다.
                matchCount = 0;
                whiteCount = 0;
                blackCount = 0;
                totalCountWhite = 0;
                totalCountBlack = 0;
                for (int y = 0; y < patternHeight - jumpY; y += jumpY) {
                    for (int x = 0; x < patternWidth - jumpX; x += jumpX) {
                        graySource = pSourceBuffer[(startY + y) * sourceWidth + startX + x];
                        grayPattern = pPatternBuffer[y * patternWidth + x];
                        //////  Pattern과 Source의 Gray Level이 같은 경우 match Count를 늘린다.
                        if (grayPattern == graySource) matchCount++;
                        if (grayPattern == 0) {
                            totalCountBlack++;
                            if (grayPattern == graySource)
                                blackCount++;
                        }
                        if (grayPattern == 255) {
                            totalCountWhite++;
                            if (grayPattern == graySource)
                                whiteCount++;
                        }
                    }
                }
                matchCount = blackCount;
                ////// 최대한 Matching IYoonVector가 많은 Pattern을 찾는다.
                if (matchCount > matchCountMax) {
                    matchCountMax = matchCount;
                    findPosX = iX;
                    findPosY = iY;
                    dCoefficient = 0.0;
                    if (totalCountBlack > totalCountWhite)
                        dCoefficient = blackCount * 100.0 / totalCountBlack;
                    else
                        dCoefficient = whiteCount * 100.0 / totalCountWhite;
                }
            }
        }
        findRect.CenterPos.setX(findPosX);
        findRect.CenterPos.setY(findPosY);
        findRect.Width = patternWidth;
        findRect.Height = patternHeight;
        return new YoonObject<YoonRect2N>(0, findRect, dCoefficient, matchCountMax);
    }

    public static YoonObject findPattern(YoonImage pPatternImage, YoonImage pSourceImage, int nDiffThreshold) throws IOException {
        if (pPatternImage.getPlane() == 1 && pSourceImage.getPlane() == 1)
            return findPattern(pPatternImage.toByteArray(), pPatternImage.getWidth(), pPatternImage.getHeight(), pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), (byte) nDiffThreshold);
        else if (pPatternImage.getPlane() == 4 && pSourceImage.getPlane() == 4)
            return findPattern(pPatternImage.toIntegerArray(), pPatternImage.getWidth(), pPatternImage.getHeight(), pSourceImage.toIntegerArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nDiffThreshold);
        else
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image format arguments is not comportable");
    }

    public static YoonObject findPattern(byte[] pPatternBuffer, int patternWidth, int patternHeight, byte[] pSourceBuffer, int sourceWidth, int sourceHeight, byte diffThreshold) {
        int minDiff, sumDiff;
        int count, findPosX, findPosY;
        int graySource, grayPattern;
        int startX, startY, jumpX, jumpY;
        double dCoefficient = 0.0;
        ////  초기화
        YoonRect2N findRect = new YoonRect2N(0, 0, 0, 0);
        minDiff = 2147483647;
        sumDiff = 0;
        count = 0;
        findPosX = 0;
        findPosY = 0;
        ////  Skip 정도 지정
        jumpX = patternWidth / 30;
        jumpY = patternHeight / 30;
        if (jumpX < 1) jumpX = 1;
        if (jumpY < 1) jumpY = 1;
        ////  Match 갯수 및 정도를 찾는다.
        for (int iY = 0; iY < sourceHeight - patternHeight; iY += 1) {
            for (int iX = 0; iX < sourceWidth - patternWidth; iX += 1) {
                startX = iX;
                startY = iY;
                ////// 전체 영역 내에서의 차이 값을 구한다.
                sumDiff = 0;
                for (int y = 0; y < patternHeight - jumpY; y += jumpY) {
                    for (int x = 0; x < patternWidth - jumpX; x += jumpX) {
                        graySource = pSourceBuffer[(startY + y) * sourceWidth + startX + x];
                        grayPattern = pPatternBuffer[y * patternWidth + x];
                        sumDiff += Math.abs(graySource - grayPattern);
                        if (Math.abs(graySource - grayPattern) < diffThreshold)
                            count++;
                    }
                }
                ////// Diff가 최소인 지점을 찾는다.
                if (sumDiff < minDiff) {
                    minDiff = sumDiff;
                    findPosX = iX;
                    findPosY = iY;
                }
            }
        }
        findRect.CenterPos.setX(findPosX);
        findRect.CenterPos.setY(findPosY);
        findRect.Width = patternWidth;
        findRect.Height = patternHeight;
        ////  상관계수 구하기
        byte[] pTempBuffer;
        pTempBuffer = new byte[patternWidth * patternHeight];
        for (int j = 0; j < patternHeight; j++)
            for (int i = 0; i < patternWidth; i++)
                pTempBuffer[j * patternWidth + i] = pSourceBuffer[(findPosY + j) * sourceWidth + (findPosX + i)];
        dCoefficient = MathFactory.getCorrelationCoefficient(pPatternBuffer, pTempBuffer, patternWidth, patternHeight);

        return new YoonObject<YoonRect2N>(0, findRect, dCoefficient, count);
    }

    public static YoonObject<YoonRect2N> findPattern(int[] pPatternBuffer, int patternWidth, int patternHeight, int[] pSourceBuffer, int sourceWidth, int sourceHeight, int diffThreshold) {
        int minDiff, sumDiff;
        int count, findPosX, findPosY;
        int graySource, grayPattern;
        int startX, startY, jumpX, jumpY;
        double dCoefficient = 0.0;
        ////  초기화
        YoonRect2N findRect = new YoonRect2N(0, 0, 0, 0);
        minDiff = 2147483647;
        sumDiff = 0;
        count = 0;
        findPosX = 0;
        findPosY = 0;
        ////  Skip 정도 지정
        jumpX = patternWidth / 60;
        jumpY = patternHeight / 60;
        if (jumpX < 1) jumpX = 1;
        if (jumpY < 1) jumpY = 1;
        ////  Match 갯수 및 정도를 찾는다.
        for (int iY = 0; iY < sourceHeight - patternHeight; iY += 1) {
            for (int iX = 0; iX < sourceWidth - patternWidth; iX += 1) {
                startX = iX;
                startY = iY;
                ////// 전체 영역 내에서의 차이 값을 구한다.
                sumDiff = 0;
                for (int y = 0; y < patternHeight - jumpY; y += jumpY) {
                    for (int x = 0; x < patternWidth - jumpX; x += jumpX) {
                        graySource = pSourceBuffer[(startY + y) * sourceWidth + startX + x];
                        grayPattern = pPatternBuffer[y * patternWidth + x];
                        sumDiff += Math.abs(graySource - grayPattern);
                        if (Math.abs(graySource - grayPattern) < diffThreshold)
                            count++;
                    }
                }
                ////// Diff가 최소인 지점을 찾는다.
                if (sumDiff < minDiff) {
                    minDiff = sumDiff;
                    findPosX = iX;
                    findPosY = iY;
                }
            }
        }
        findRect.CenterPos.setX(findPosX);
        findRect.CenterPos.setY(findPosY);
        findRect.Width = patternWidth;
        findRect.Height = patternHeight;
        ////  상관계수 구하기
        int[] pTempBuffer;
        pTempBuffer = new int[patternWidth * patternHeight];
        for (int j = 0; j < patternHeight; j++)
            for (int i = 0; i < patternWidth; i++)
                pTempBuffer[j * patternWidth + i] = pSourceBuffer[(findPosY + j) * sourceWidth + (findPosX + i)];
        dCoefficient = MathFactory.getCorrelationCoefficient(pPatternBuffer, pTempBuffer, patternWidth, patternHeight);

        return new YoonObject<YoonRect2N>(0, findRect, dCoefficient, count);
    }

    public static YoonObject findPattern(YoonRect2N scanArea, YoonImage pPatternImage, YoonImage pSourceImage, int nDiffThreshold) throws IOException {
        if (pPatternImage.getPlane() != 1 || pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        if (!pPatternImage.isVerifiedArea(scanArea))
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Scan area is not verified");
        return findPattern(scanArea, pPatternImage.toByteArray(), pPatternImage.getWidth(), pPatternImage.getHeight(), pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nDiffThreshold);
    }

    public static YoonObject findPattern(YoonRect2N scanArea, byte[] pPatternBuffer, int patternWidth, int patternHeight, byte[] pSourceBuffer, int sourceWidth, int sourceHeight, int diffThreshold) {
        int minDiff, sumDiff;
        int count, findPosX, findPosY;
        int graySource, grayPattern;
        int startX, startY, jumpX, jumpY;
        double dCoefficient = 0.0;
        ////  초기화
        YoonRect2N findRect = new YoonRect2N(0, 0, 0, 0);
        if (patternWidth < 1 || patternHeight < 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Pattern size is not verified");
        minDiff = 2147483647;
        sumDiff = 0;
        count = 0;
        findPosX = 0;
        findPosY = 0;
        ////  Skip 정도 지정
        jumpX = patternWidth / 60;
        jumpY = patternHeight / 60;
        if (jumpX < 1) jumpX = 1;
        if (jumpY < 1) jumpY = 1;
        for (int iY = scanArea.getTop(); iY < scanArea.getBottom() - patternHeight; iY += 2) {
            for (int iX = scanArea.getLeft(); iX < scanArea.getRight() - patternWidth; iX += 2) {
                startX = iX;
                startY = iY;
                ////// 전체 영역 내에서의 차이 값을 구한다.
                sumDiff = 0;
                for (int y = 0; y < patternHeight - jumpY; y += jumpY) {
                    for (int x = 0; x < patternWidth - jumpX; x += jumpX) {
                        graySource = pSourceBuffer[(startY + y) * sourceWidth + startX + x];
                        grayPattern = pPatternBuffer[y * patternWidth + x];
                        sumDiff += Math.abs(graySource - grayPattern);
                        if (Math.abs(graySource - grayPattern) < diffThreshold)
                            count++;
                    }
                }
                ////// Diff가 최소인 지점을 찾는다.
                if (sumDiff < minDiff) {
                    minDiff = sumDiff;
                    findPosX = iX;
                    findPosY = iY;
                }
            }
        }
        findRect.CenterPos.setX(findPosX);
        findRect.CenterPos.setY(findPosY);
        findRect.Width = patternWidth;
        findRect.Height = patternHeight;
        ////  상관계수 구하기
        byte[] pTempBuffer;
        pTempBuffer = new byte[patternWidth * patternHeight];
        for (int j = 0; j < patternHeight; j++)
            for (int i = 0; i < patternWidth; i++)
                pTempBuffer[j * patternWidth + i] = pSourceBuffer[(findPosY + j) * sourceWidth + (findPosX + i)];
        dCoefficient = MathFactory.getCorrelationCoefficient(pPatternBuffer, pTempBuffer, patternWidth, patternHeight);

        return new YoonObject<YoonRect2N>(0, findRect, dCoefficient, count);
    }
}
