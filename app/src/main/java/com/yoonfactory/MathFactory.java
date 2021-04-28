package com.yoonfactory;

import java.util.concurrent.atomic.AtomicReference;

public class MathFactory {
    public static double getCorrelationCoefficient(byte[] pBuffer1, byte[] pBuffer2, int width, int height)
    {
        int valueBuffer1, valueBuffer2;
        int diffBuffer1, diffBuffer2;
        double averageBuffer1, averageBuffer2;
        int sumBuffer1, sumBuffer2, sumDiff;
        double dx, dy, dxy;
        double coefficient;
        int size;
        //// Pattern의 평균값을 구한다.
        size = width * height;
        sumBuffer1 = 0;
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                valueBuffer1 = pBuffer1[j * width + i];
                sumBuffer1 += valueBuffer1;
            }
        }
        averageBuffer1 = (double)sumBuffer1 / (double)size;
        ////  현 위치의 평균값을 구한다.
        sumBuffer2 = 0;
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                valueBuffer2 = pBuffer2[j * width + i];
                sumBuffer2 += valueBuffer2;
            }
        }
        averageBuffer2 = (double)sumBuffer2 / (double)size;
        ////  상관계수 구하기.
        dx = 0;
        dy = 0;
        dxy = 0;
        sumDiff = 0;
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                valueBuffer1 = pBuffer1[j * width + i];
                valueBuffer2 = pBuffer2[j * width + i];
                diffBuffer1 = valueBuffer1 - (int)averageBuffer1;
                diffBuffer2 = valueBuffer2 - (int)averageBuffer2;
                // 상관계수 산출식.
                dx += diffBuffer1 * diffBuffer1;
                dy += diffBuffer2 * diffBuffer2;
                dxy += diffBuffer1 * diffBuffer2;
                sumDiff += Math.abs(diffBuffer1 - diffBuffer2);
            }
        }
        ////  상관계수 방정식 및 값 산출.
        double a = Math.sqrt(dx * dy);
        double b = dxy;
        if (Math.abs(a) < 1)
        {
            a = 1;
        }
        coefficient = b * 100.0 / a;
        return coefficient;
    }

    public static double getCorrelationCoefficient(int[] pBuffer1, int[] pBuffer2, int width, int height)
    {
        int valueBuffer1, valueBuffer2;
        int diffBuffer1, diffBuffer2;
        double averageBuffer1, averageBuffer2;
        int sumBuffer1, sumBuffer2, sumDiff;
        double dx, dy, dxy;
        double coefficient;
        int size;
        //// Pattern의 평균값을 구한다.
        size = width * height;
        sumBuffer1 = 0;
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                valueBuffer1 = pBuffer1[j * width + i];
                sumBuffer1 += valueBuffer1;
            }
        }
        averageBuffer1 = (double)sumBuffer1 / (double)size;
        ////  현 위치의 평균값을 구한다.
        sumBuffer2 = 0;
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                valueBuffer2 = pBuffer2[j * width + i];
                sumBuffer2 += valueBuffer2;
            }
        }
        averageBuffer2 = (double)sumBuffer2 / (double)size;
        ////  상관계수 구하기.
        dx = 0;
        dy = 0;
        dxy = 0;
        sumDiff = 0;
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                valueBuffer1 = pBuffer1[j * width + i];
                valueBuffer2 = pBuffer2[j * width + i];
                diffBuffer1 = valueBuffer1 - (int)averageBuffer1;
                diffBuffer2 = valueBuffer2 - (int)averageBuffer2;
                // 상관계수 산출식.
                dx += diffBuffer1 * diffBuffer1;
                dy += diffBuffer2 * diffBuffer2;
                dxy += diffBuffer1 * diffBuffer2;
                sumDiff += Math.abs(diffBuffer1 - diffBuffer2);
            }
        }
        ////  상관계수 방정식 및 값 산출.
        double a = Math.sqrt(dx * dy);
        double b = dxy;
        if (Math.abs(a) < 1)
        {
            a = 1;
        }
        coefficient = b * 100.0 / a;
        return coefficient;
    }

    //  최소자승법.  (Y = (Slope)X + (intercept))
    public static boolean leastSquare(AtomicReference<Double> refSlope, AtomicReference<Double> refIntercept, int number, double[] pX, double[] pY)
    {
        boolean isResult;
        double differX, differY;
        double sumX, sumX2, sumXY, sumY;
        double a, b, c;
        double dSlope, dIntercept;
        isResult = true;
        ////  Data가 비정상적인지 조사함.
        for (int i = 0; i < number; i++)
        {
            if (pX[i] > 10000 || pY[i] > 10000)
            {
                isResult = false;
                break;
            }
        }
        if (isResult == false)
        {
            refSlope.set(0.0);
            refIntercept.set(0.0);
            return false;
        }
        if (number < 2 || number > 10)
        {
            refSlope.set(0.0);
            refIntercept.set(0.0);
            return false;
        }
        ////  최소자승법 계산.
        if (number == 2)
        {
            differX = pX[1] - pX[0];
            differY = pY[1] - pY[0];
            if (Math.abs(differX) < 0.0001)
                differX = 0.0001;
            dIntercept = differY / differX;
            dSlope = pY[0] - dIntercept * pX[0];
        }
        ////  다차원을 가질 경우. (number가 10 이상)
        else
        {
            sumX = 0.00;
            sumX2 = 0.00;
            sumXY = 0.00;
            sumY = 0.00;
            for (int i = 0; i < number; i++)
            {
                sumX += pX[i];
                sumX2 += pX[i] * pX[i];
                sumXY += pX[i] * pY[i];
                sumY += pY[i];
            }
            c = sumX * sumX - (double)number * sumX2;
            if (c == 0.0)
                return false;
            b = (sumX * sumY - sumXY * (double)number) / c;
            a = (sumY - b * sumX) / number;
            dSlope = a;
            dIntercept = b;
        }
        refSlope.set(dSlope);
        refIntercept.set(dIntercept);
        return true;
    }

    public static boolean leastSquare(AtomicReference<Double> refSlope, AtomicReference<Double> refIntercept, int number, int[] pX, int[] pY)
    {
        boolean isResult;
        double differX, differY;
        double sumX, sumX2, sumXY, sumY;
        double a, b, c;
        double dSlope, dIntercept;
        isResult = true;
        ////  Data가 비정상적인지 조사함.
        for (int i = 0; i < number; i++)
        {
            if (pX[i] > 10000 || pY[i] > 10000)
            {
                isResult = false;
                break;
            }
        }
        if (isResult == false)
        {
            refSlope.set(0.0);
            refIntercept.set(0.0);
            return false;
        }
        if (number < 2 || number > 10)
        {
            refSlope.set(0.0);
            refIntercept.set(0.0);
            return false;
        }
        ////  최소자승법 계산.
        if (number == 2)
        {
            differX = pX[1] - pX[0];
            differY = pY[1] - pY[0];
            if (Math.abs(differX) < 0.0001)
                differX = 0.0001;
            dIntercept = differY / differX;
            dSlope = pY[0] - dIntercept * pX[0];
        }
        ////  다차원을 가질 경우. (number가 10 이상)
        else
        {
            sumX = 0.00;
            sumX2 = 0.00;
            sumXY = 0.00;
            sumY = 0.00;
            for (int i = 0; i < number; i++)
            {
                sumX += pX[i];
                sumX2 += pX[i] * pX[i];
                sumXY += pX[i] * pY[i];
                sumY += pY[i];
            }
            c = sumX * sumX - (double)number * sumX2;
            if (c == 0.0)
                return false;
            b = (sumX * sumY - sumXY * (double)number) / c;
            a = (sumY - b * sumX) / number;
            dSlope = a;
            dIntercept = b;
        }
        refSlope.set(dSlope);
        refIntercept.set(dIntercept);
        return true;
    }

    //  N개의 점으로 N-1차 함수를 구하는 Lagrange 공식.
    //  A[0] + A[1]X + A[2]X^2 ...
    public static boolean lagrange(AtomicReference<double[]> refA, AtomicReference<double[]> refX, AtomicReference<double[]> refY, int number)
    {
        int i, j;
        double sign, differX;
        double coefficient, tempValue;
        double[] pA = refA.get();
        double[] pX = refX.get();
        double[] pY = refY.get();
        for (i = 0; i < number; i++)
            pA[i] = 0.0;
        for (i = 0; i < number; i++)
        {
            if (pY[i] == 0.0) continue;
            coefficient = pY[i];
            for (j = 0; j < number; j++)
            {
                if (i == j) continue;
                if (pX[i] == pX[j]) return false;
                differX = pX[i] - pX[j];
                coefficient /= differX;
            }
            tempValue = pX[i];
            pX[i] = 0.0;
            for (j = 0; j < number; j++)
            {
                if (j % 2 != 0) sign = -1.0;
                else sign = 1.0;
                pA[number - j - 1] += sign * coefficient * Combine(pX, pY, number, j);
            }
            pX[i] = tempValue;
        }
        refA.set(pA);
        refX.set(pX);
        refY.set(pY);
        return true;
    }

    //  조합.  nCr.
    public static double Combine(double[] pX, double[] pY, int number, int rootNum)
    {
        int i;
        double result;
        result = 1.0;
        if (number <= 0 || rootNum < 0 || number < rootNum)
            return 0.0;
        ////  nCn 이므로 일종의 순열과 같다고 보면 됨.
        if (number == rootNum)
        {
            for (i = 0; i < number; i++)
                result *= pX[i];
            return result;
        }
        ////  nC0은 1이 됨.
        if (rootNum == 0)
            return 1.0;
        ////  조합 공식.
        result = pX[number - 1] * Combine(pX, pY, number - 1, rootNum - 1) + Combine(pX, pY, number - 1, rootNum);
        return result;
    }
}
