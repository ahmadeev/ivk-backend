package ru.ivk.utils.math;

public class PlaneMath {
    /** Возвращает вершину прямого угла треугольника, если такой существует */
    public static Coordinates getRightAngleVertex(Coordinates p1, Coordinates p2, Coordinates p3) {
        double d12 = getTwoPointsDistance(p1, p2);
        double d13 = getTwoPointsDistance(p1, p3);
        double d23 = getTwoPointsDistance(p2, p3);

        if (Double.compare(d12, d13) == 0 && isRightAngleTriangle(d23, d12, d13)) return p1;
        if (Double.compare(d12, d23) == 0 && isRightAngleTriangle(d13, d12, d23)) return p2;
        if (Double.compare(d13, d23) == 0 && isRightAngleTriangle(d12, d13, d23)) return p3;

        return null;
    }

    /** По переданным длинам отрезков определяет, является ли треугольник прямоугольным */
    public static boolean isRightAngleTriangle(double hypotenuse, double leg1, double leg2) {
        return Double.compare(java.lang.Math.pow(leg1, 2) + java.lang.Math.pow(leg2, 2), java.lang.Math.pow(hypotenuse, 2)) == 0;
    }

    /** Возвращает расстояние между двумя точками на плоскости */
    public static double getTwoPointsDistance(Coordinates p1, Coordinates p2) {
        if (p1.equals(p2)) return 0;
        return java.lang.Math.sqrt(
                java.lang.Math.pow(p1.getX() - p2.getX(), 2) + java.lang.Math.pow(p1.getY() - p2.getY(), 2)
        );
    }
}
