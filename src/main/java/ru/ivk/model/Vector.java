package ru.ivk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Vector {
    private final Coordinates point1;
    private final Coordinates point2;
    private final int x;
    private final int y;
    private final double length;

    public Vector(Coordinates point1, Coordinates point2) {
        this.point1 = point1;
        this.point2 = point2;
        this.x = point2.getX() - point1.getX();
        this.y = point2.getY() - point1.getY();
        // todo: замена на функцию
        this.length = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector(int x, int y) {
        this.point1 = null;
        this.point2 = null;
        this.x = x;
        this.y = y;
        this.length = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public static Vector sum(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }
}
