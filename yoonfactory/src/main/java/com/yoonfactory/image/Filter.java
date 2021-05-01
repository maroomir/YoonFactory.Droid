package com.yoonfactory.image;

import java.util.concurrent.atomic.AtomicReference;

public class Filter {

    public static YoonImage sobel(YoonImage pSourceImage, int nIntensity, boolean bCombine) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(sobel(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nIntensity, bCombine),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] sobel(byte[] pBuffer, int width, int height, int nIntensity, boolean bCombineSource) {
        int x, y, i, j;
        int posX, posY;
        int imageWidth, imageHeight, imageSize;
        int centerValue1, centerValue2, sum, value;
        byte[] pResultBuffer;
        ////  Sobel Mask 생성.
        int maskValue = nIntensity;
        int[][] mask1 = {{-maskValue, 0, maskValue},
                {-maskValue, 0, maskValue},
                {-maskValue, 0, maskValue}};
        int[][] mask2 = {{maskValue, maskValue, maskValue},
                {0, 0, 0},
                {-maskValue, -maskValue, -maskValue}};
        imageWidth = width;
        imageHeight = height;
        imageSize = imageWidth * imageHeight;
        pResultBuffer = new byte[imageSize];
        ////  Sobel Mask 처리.
        for (y = 0; y < height - 2; y++) {
            for (x = 0; x < width - 2; x++) {
                centerValue1 = 0;
                centerValue2 = 0;
                for (j = 0; j < 3; j++) {
                    for (i = 0; i < 3; i++) {
                        posX = x + i;
                        posY = y + j;
                        value = pBuffer[posY * imageWidth + posX];
                        centerValue1 += value * mask1[i][j];
                        centerValue2 += value * mask2[i][j];
                    }
                }
                sum = Math.abs(centerValue1) + Math.abs(centerValue2);

                if (sum > 255) sum = 255;
                if (sum < 0) sum = 0;
                posX = x + 1;
                posY = y + 1;
                pResultBuffer[posY * imageWidth + posX] = (byte) sum;
            }
        }
        ////  Sobel Filtering 결과와 원본을 합친 영상을 원하는 경우.
        if (bCombineSource) {
            pResultBuffer = TwoImageProcess.combine(pBuffer, pResultBuffer, width, height);
        }
        return pResultBuffer;
    }

    public static YoonImage laplacian(YoonImage pSourceImage, int nIntensity, boolean bCombine) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(laplacian(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nIntensity, bCombine),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] laplacian(byte[] pBuffer, int width, int height, int Intensity, boolean bCombineSource) {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Buffer size is not normalized");
        int i, j;
        int centerValue, value;
        byte[] pResultBuffer;
        centerValue = 4 * Intensity;
        pResultBuffer = new byte[width * height];
        ////  Laplacian Mask 처리.
        for (j = 1; j < height - 1; j++) {
            for (i = 1; i < width - 1; i++) {
                value = centerValue * pBuffer[j * width + i] - pBuffer[(j - 1) * width + i] - pBuffer[j * width + i + 1] - pBuffer[(j + 1) * width + i] - pBuffer[j * width + i - 1];
                if (value < 0) value = 0;
                if (value > 255) value = 255;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        ////  Laplacian 결과와 원본 영상을 합친 경우.
        if (bCombineSource) {
            pResultBuffer = TwoImageProcess.combine(pBuffer, pResultBuffer, width, height);
        }
        return pResultBuffer;
    }

    public static YoonImage rc1D(YoonImage pSourceImage, double dFrequency, boolean bCombine) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(rc1D(pSourceImage.toByteArray(), pSourceImage.getWidth(), dFrequency, bCombine),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] rc1D(byte[] pBuffer, int size, double frequency, boolean bCombineSource) {
        int i, j;
        double value;
        int width, height;
        double[] pWidth, pHeight;
        byte[] pResultBuffer;
        width = size;
        height = 1;
        pWidth = new double[width];
        pHeight = new double[height];
        pResultBuffer = new byte[width * height];
        ////  가로방향 Filtering
        for (j = 0; j < height; j++) {
            pHeight[j] = pBuffer[j * width + 0];
            pResultBuffer[j * width + 0] = (byte) (0.5 * pHeight[j]);
            for (i = 1; i < width; i++) {
                pHeight[j] = frequency * pHeight[j] + (1 - frequency) * pBuffer[j * width + i];
                value = 0.5 * pHeight[j];
                if (value < 0) value = 0;
                if (value > 255) value = 255;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        for (j = 0; j < height; j++) {
            pHeight[j] = pBuffer[j * width + (width - 1)];
            value = 0.5 * pHeight[j] + pResultBuffer[j * width + (width - 1)];
            if (value < 0) value = 0;
            if (value > 255) value = 255;
            pResultBuffer[j * width + (width - 1)] = (byte) value;
            for (i = width - 2; i >= 0; i--) {
                pHeight[j] = frequency * pHeight[j] + (1 - frequency) * pBuffer[j * width + i];
                value = pResultBuffer[j * width + i] + 0.5 * pHeight[j];
                if (value < 0) value = 0;
                if (value > 255) value = 255;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        ////  RC 결과와 원본 영상을 합친 경우.
        if (bCombineSource) {
            pResultBuffer = TwoImageProcess.combine(pBuffer, pResultBuffer, width, height);
        }
        return pResultBuffer;
    }

    public static YoonImage rc2D(YoonImage pSourceImage, double dFrequency, boolean bCombine) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(rc2D(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), dFrequency, bCombine),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] rc2D(byte[] pBuffer, int width, int height, double frequency, boolean bSumSource) {
        int i, j;
        double value;
        double[] pWidth, pHeight;
        byte[] pResultBuffer;
        pWidth = new double[width];
        pHeight = new double[height];
        pResultBuffer = new byte[width * height];
        ////  가로방향 Filtering
        for (j = 0; j < height; j++) {
            pHeight[j] = pBuffer[j * width + 0];
            pResultBuffer[j * width + 0] = (byte) (0.5 * pHeight[j]);
            for (i = 1; i < width; i++) {
                pHeight[j] = frequency * pHeight[j] + (1 - frequency) * pBuffer[j * width + i];
                value = 0.5 * pHeight[j];
                if (value < 0) value = 0;
                if (value > 255) value = 255;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        for (j = 0; j < height; j++) {
            pHeight[j] = pBuffer[j * width + (width - 1)];
            value = 0.5 * pHeight[j] + pResultBuffer[j * width + (width - 1)];
            if (value < 0) value = 0;
            if (value > 255) value = 255;
            pResultBuffer[j * width + (width - 1)] = (byte) value;
            for (i = width - 2; i >= 0; i--) {
                pHeight[j] = frequency * pHeight[j] + (1 - frequency) * pBuffer[j * width + i];
                value = pResultBuffer[j * width + i] + 0.5 * pHeight[j];
                if (value < 0) value = 0;
                if (value > 255) value = 255;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        ////  세로방향 Filtering
        for (i = 0; i < width; i++) {
            pWidth[i] = pResultBuffer[0 * width + i];
            pResultBuffer[0 * width + i] = (byte) (0.5 * pWidth[i]);
            for (j = 1; j < height; j++) {
                pWidth[i] = frequency * pWidth[i] + (1 - frequency) * pResultBuffer[j * width + i];
                value = 0.5 * pWidth[i];
                if (value < 0) value = 0;
                if (value > 255) value = 255;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        for (i = 0; i < width; i++) {
            pWidth[i] = pResultBuffer[(height - 1) * width + i];
            value = 0.5 * pWidth[i] + pResultBuffer[(height - 1) * width + i];
            if (value < 0) value = 0;
            if (value > 255) value = 255;
            pResultBuffer[(height - 1) * width + i] = (byte) value;
            for (j = height - 2; j >= 0; j--) {
                pWidth[i] = frequency * pWidth[i] + (1 - frequency) * pResultBuffer[j * width + i];
                value = pResultBuffer[j * width + i] + 0.5 * pWidth[i];
                if (value < 0) value = 0;
                if (value > 255) value = 255;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        ////  RC 결과와 원본 영상을 합친 경우.
        if (bSumSource) {
            pResultBuffer = TwoImageProcess.combine(pBuffer, pResultBuffer, width, height);
        }
        return pResultBuffer;
    }

    public static YoonImage level2D(YoonImage pSourceImage, AtomicReference<Double> refSum) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(level2D(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), refSum),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] level2D(byte[] pBuffer, int width, int height, AtomicReference<Double> refSum) {
        double diffDx, diffDy;
        double inverseDx, inverseDy;
        int centerX, centerY;
        int count;
        int x1, x2, y1, y2;
        double[][] pAverage = new double[2][2];
        double value;
        byte[] pResultBuffer = new byte[width * height];
        double dSum = refSum.get();
        inverseDx = 1 / (double) width;
        inverseDy = 1 / (double) height;
        centerX = width / 2;
        centerY = height / 2;
        count = 0;
        dSum = 0;
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        ////  Level 필터 적용하기.
        for (int iNo = 0; iNo < 4; iNo++) {
            int iRow = iNo / 2;
            int iCol = iNo % 2;
            count = 0;
            //////  Source 크기에 맞게 각기 다른 4개의 Filter 배열 생성하기.
            switch (iNo) {
                case 0:
                    x1 = 2;
                    y1 = 2;
                    x2 = centerX / 2 - 2;
                    y2 = centerY / 2 - 2;
                    break;
                case 1:
                    x1 = centerX + centerX / 2;
                    x2 = width - 2;
                    y1 = 2;
                    y2 = centerY / 2 - 2;
                    break;
                case 2:
                    x1 = 2;
                    y1 = centerY + centerY / 2;
                    x2 = centerX / 2 - 2;
                    y2 = height - 2;
                    break;
                case 3:
                    x1 = centerX + centerX / 2;
                    y1 = centerY + centerY / 2;
                    x2 = width - 2;
                    y2 = height - 2;
                    break;
            }
            //////  4개 Filter를 통과한 배열의 평균값 구하기.
            for (int j = y1; j < y2; j += 2) {
                for (int i = x1; i < x2; i += 2) {
                    pAverage[iRow][iCol] = pAverage[iRow][iCol] + pBuffer[j * width + i];
                    count++;
                }
            }
            ////  4가지 Filter 간의 평균값 구하기.
            if (count > 1) pAverage[iRow][iCol] = pAverage[iRow][iCol] / (float) count;
            else pAverage[iRow][iCol] = 0;
            dSum += pAverage[iRow][iCol];
        }
        dSum /= (float) 4.0;
        //// 역수곱 계산
        diffDx = inverseDx * (pAverage[1][0] + pAverage[1][1] - pAverage[0][0] - pAverage[0][1]);
        diffDy = inverseDy * (pAverage[0][1] + pAverage[1][1] - pAverage[0][0] - pAverage[1][0]);
        ////  Filtering 결과 출력.
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                value = 100 * pBuffer[j * width + i] / (dSum + diffDx * (i - centerX) + diffDy * (j - centerY));
                if (value > 255) value = 255;
                if (value < 0) value = 0;
                pResultBuffer[j * width + i] = (byte) value;
            }
        }
        refSum.set(dSum);
        return pResultBuffer;
    }

    public static YoonImage demargin2D(YoonImage pSourceImage) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(demargin2D(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight()),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] demargin2D(byte[] pBuffer, int width, int height) {
        int centerX, centerY;
        float[] pWidth, pHeight;
        float norm;
        byte[] pResultBuffer = new byte[width * height];
        centerX = width / 2;
        centerY = height / 2;
        pWidth = new float[width];
        pHeight = new float[height];
        ////  각 행별 Data를 Filter에 더하기.
        for (int j = 0; j < height; j++) {
            pHeight[j] = pBuffer[j * width + 0];
            for (int i = 1; i < width; i++) {
                pHeight[j] = pHeight[j] + pBuffer[j * width + i];
            }
        }
        ////  Filter 중심 기준의 배율로 Filter 덮어쓰기.
        norm = pHeight[centerY];
        for (int j = 0; j < height; j++) {
            if (pHeight[j] > 1)
                pHeight[j] = norm / pHeight[j];
            else
                pHeight[j] = 1;
        }
        ////  세로 방향 Filter를 Buffer에 적용하기.
        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++)
                pResultBuffer[j * width + i] = (byte) (pHeight[j] * pBuffer[j * width + i]);
        ////  각 열별 Data를 Filter에 더하기.
        for (int i = 0; i < width; i++) {
            pWidth[i] = pResultBuffer[0 * width + i];
            for (int j = 0; j < height; j++) {
                pWidth[i] = pWidth[i] + pResultBuffer[j * width + i];
            }
        }
        ////  Filter 중심 기준의 배율로 Filter 덮어쓰기.
        norm = pWidth[centerX];
        for (int i = 0; i < width; i++) {
            if (pWidth[i] > 0)
                pWidth[i] = norm / pWidth[i];
            else
                pWidth[i] = 1;
        }
        ////  가로 방향 Filter를 Buffer에 적용하기.
        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++)
                pResultBuffer[j * width + i] = (byte) (pWidth[i] * pResultBuffer[j * width + i]);
        return pResultBuffer;
    }

    public static YoonImage smooth1D(YoonImage pSourceImage, int nMargin, int nStep) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(smooth1D(pSourceImage.toByteArray(), pSourceImage.getWidth() * pSourceImage.getHeight(), nMargin, nStep),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] smooth1D(byte[] pBuffer, int bufferSize, int margin, int step) {
        int x, i, ii;
        int sum, count;
        byte[] pResultBuffer = new byte[bufferSize];
        sum = 0;
        count = 0;
        if (step < 1)
            step = 1;
        for (i = margin; i < bufferSize - margin; i++) {
            sum = 0;
            count = 0;
            ////  (marginX2) 정도의 크기만큼을 Sampling 해야한다.
            for (ii = -margin; ii <= margin; ii += step) {
                x = i + ii;
                if (x >= bufferSize)
                    continue;
                sum += pBuffer[x];
                count++;
            }
            if (count < 1)
                count = 1;
            //////  Sampling한 주변 Pixel들의 Gray Level을 Buffer에 넣는다. (평균값 필터)
            pResultBuffer[i] = (byte) (sum / count);
        }
        return pResultBuffer;
    }

    public static YoonImage smooth2D(YoonImage pSourceImage, int nStep) {
        if (pSourceImage.getPlane() != 1)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Image arguments is not 8bit format");
        return new YoonImage(smooth2D(pSourceImage.toByteArray(), pSourceImage.getWidth(), pSourceImage.getHeight(), nStep),
                pSourceImage.getWidth(), pSourceImage.getHeight());
    }

    public static byte[] smooth2D(byte[] pBuffer, int width, int height, int nBlurStep) {
        if (width < 1 || height < 1 || pBuffer == null)
            throw new IllegalArgumentException("[YOONIMAGE EXCEPTION] Buffer size is not normalized");
        int i, j, ii, jj, x, y;
        int count, sum;
        int stepSize;
        byte[] pResultBuffer = new byte[width * height];
        stepSize = 2 * nBlurStep / 10;
        if (stepSize < 1)
            stepSize = 1;
        ////  각 Pixel마다 평균값 필터 씌우기.
        for (j = 0; j < height; j++) {
            count = 0;
            sum = 0;
            for (i = 0; i < width; i++) {
                sum = 0;
                count = 0;
                //////  각 Pixel에서 좌우로 Blur Size만큼의 Sampling Pixel을 추출해서 평균값을 구한다.
                for (jj = -nBlurStep; jj <= nBlurStep; jj += stepSize) {
                    y = j + jj;
                    for (ii = -nBlurStep; ii <= nBlurStep; ii += stepSize) {
                        x = i + ii;
                        if (x < 0 || y < 0 || x >= width || y >= height)
                            continue;
                        sum += pBuffer[y * width + x];
                        count++;
                    }
                }
                if (count < 1)
                    count = 1;
                //////  Sampling으로 구한 평균값을 그대로 각 Pixel에 씌운다.
                pResultBuffer[j * width + i] = (byte) (sum / count);
            }
        }
        return pResultBuffer;
    }
}