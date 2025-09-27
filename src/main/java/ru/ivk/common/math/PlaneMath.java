package ru.ivk.common.math;

import java.math.BigDecimal;
import java.util.Arrays;

public class PlaneMath {
    /** Возвращает вершину прямого угла треугольника, если такой существует */
    public static Coordinates getRightAngleVertex(Coordinates p1, Coordinates p2, Coordinates p3) {
        Vector v11 = new Vector(p1, p2);
        Vector v12 = new Vector(p1, p3);
        double sp1 = Vector.scalarProduct(v11, v12);
        if (Double.compare(sp1, 0) == 0) {
            return p1;
        }
        Vector v21 = new Vector(p2, p1);
        Vector v22 = new Vector(p2, p3);
        double sp2 = Vector.scalarProduct(v21, v22);
        if (Double.compare(sp2, 0) == 0) {
            return p2;
        }
        Vector v31 = new Vector(p3, p1);
        Vector v32 = new Vector(p3, p2);
        double sp3 = Vector.scalarProduct(v31, v32);
        if (Double.compare(sp3, 0) == 0) {
            return p3;
        }
        return null;
    }

    /** По переданным длинам отрезков определяет, является ли треугольник прямоугольным */
    public static boolean isRightAngleTriangle(double a, double b, double c) {
        double[] arr = new double[]{a, b, c};
        Arrays.sort(arr);
        BigDecimal aa = BigDecimal.valueOf(arr[0]);
        BigDecimal bb = BigDecimal.valueOf(arr[1]);
        BigDecimal cc = BigDecimal.valueOf(arr[2]);
        return (aa.pow(2).add(bb.pow(2))).compareTo(cc.pow(2)) == 0;
    }

    /** Возвращает расстояние между двумя точками на плоскости */
    public static double getTwoPointsDistance(Coordinates p1, Coordinates p2) {
        if (p1.equals(p2)) return 0;
        // могут возникать проблемы с точностью
        return Math.sqrt(
                Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2)
        );
    }
}
