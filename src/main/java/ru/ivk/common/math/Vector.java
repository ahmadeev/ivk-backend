package ru.ivk.common.math;

import lombok.Getter;

@Getter
public class Vector {
    private final Coordinates p1;
    private final Coordinates p2;
    private final int x;
    private final int y;
    private final double length;

    /** Использует две точки, для которых нужно найти вектор */
    public Vector(Coordinates p1, Coordinates p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.x = p2.getX() - p1.getX();
        this.y = p2.getY() - p1.getY();
        this.length = PlaneMath.getTwoPointsDistance(p1, p2);
    }

    /** Использует координаты вектора */
    public Vector(int x, int y) {
        this.p1 = null;
        this.p2 = null;
        this.x = x;
        this.y = y;
        this.length = PlaneMath.getTwoPointsDistance(new Coordinates(0, 0), new Coordinates(x, y));
    }

    public static Vector sum(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public static double scalarProduct(Vector v1, Vector v2) {
        return (v1.getX() * v2.getX() + v1.getY() * v2.getY()) / (v1.getLength() * v2.getLength());
    }
}
