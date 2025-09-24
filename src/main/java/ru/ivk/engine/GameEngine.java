package ru.ivk.engine;

import ru.ivk.enums.UserColor;
import ru.ivk.model.Coordinates;
import ru.ivk.model.Vector;
import ru.ivk.utils.GameManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// todo: движку нужна память ?
public class GameEngine {
    public static boolean isWin(Coordinates coordinates, UserColor color) {
        Map<Coordinates, String> points = getFilteredMap(coordinates, color);
        if (points.size() < 4) return false;

        for (Coordinates key : points.keySet()) {
            // выбираем пару точек. считаем, что эта пара -- соседние вершины
            if (key.equals(coordinates)) continue;
            Vector vector = new Vector(key, coordinates);
            int x0 = coordinates.getX();
            int y0 = coordinates.getY();
            int x1 = key.getX();
            int y1 = key.getY();
            int dx = vector.getX();
            int dy = vector.getY();
            // неизвестно, существуют ли в мэпе; неизвестен и цвет
            Coordinates point11 = new Coordinates(x0 + dy, y0 - dx);
            Coordinates point12 = new Coordinates(x1 + dy, y1 - dx);
            Coordinates point21 = new Coordinates(x0 - dy, y0 + dx);
            Coordinates point22 = new Coordinates(x1 - dy, y1 + dx);
            // две новые точки есть в мэпе? (если есть, то они уже нужного цвета)
            if (points.containsKey(point11) && points.containsKey(point12)) {
                return true;
            }
            if (points.containsKey(point21) && points.containsKey(point22)) {
                return true;
            }
        }
        return false;
    }

    /** Возвращает список точек (с повторами), дополняющих некоторые рамки до квадрата */
    public static List<Coordinates> findFramesOfThree(Coordinates p0, UserColor color) {
        Map<Coordinates, String> points = getFilteredMap(p0, color);
        if (points.size() < 3) return null;

        List<Coordinates> keys = new ArrayList<>(points.keySet());

        List<Coordinates> frameMissingElements = new LinkedList<>();
        for (int i = 0; i + 1 < keys.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                Coordinates p1 = keys.get(i);
                Coordinates p2 = keys.get(j);
                Coordinates rightAngleVertex = getRectangularTriangleVertex(p0, p1, p2);
                if (rightAngleVertex == null) continue;

                Vector v1;
                Vector v2;
                
                if (rightAngleVertex.equals(p0)) {
                    v1 = new Vector(p0, p1);
                    v2 = new Vector(p0, p2);
                } else if (rightAngleVertex.equals(p1)) {
                    v1 = new Vector(p1, p0);
                    v2 = new Vector(p1, p2);
                } else if (rightAngleVertex.equals(p2)) {
                    v1 = new Vector(p2, p0);
                    v2 = new Vector(p2, p1);
                } else {
                    continue;
                }

                Vector sum = Vector.sum(v1, v2);
                frameMissingElements.add(new Coordinates(
                        rightAngleVertex.getX() + sum.getX(),
                        rightAngleVertex.getY() + sum.getY()
                ));
            }
        }

        return frameMissingElements.isEmpty() ? null : frameMissingElements;
    }

    /** Возвращает вершину прямого угла треугольника, если такой существует */
    private static Coordinates getRectangularTriangleVertex(Coordinates p1, Coordinates p2, Coordinates p3) {
        double d12 = getTwoPointsDistance(p1, p2);
        double d13 = getTwoPointsDistance(p1, p3);
        double d23 = getTwoPointsDistance(p2, p3);

        if (Double.compare(d12, d13) == 0 && isPythagoreanTriangle(d23, d12, d13)) return p1;
        if (Double.compare(d12, d23) == 0 && isPythagoreanTriangle(d13, d12, d23)) return p2;
        if (Double.compare(d13, d23) == 0 && isPythagoreanTriangle(d12, d13, d23)) return p3;

        return null;
    }

    private static boolean isPythagoreanTriangle(double hypotenuse, double leg1, double leg2) {
        return Double.compare(Math.pow(leg1, 2) + Math.pow(leg2, 2), Math.pow(hypotenuse, 2)) == 0;
    }

    // todo: можно вынести в свой math package
    /** Возвращает расстояние между двумя точками на плоскости */
    private static double getTwoPointsDistance(Coordinates p1, Coordinates p2) {
        if (p1.equals(p2)) return 0;
        return Math.sqrt(
                Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2)
        );
    }

    // todo: в будущем можно засунуть в Board (тогда уйдет лишняя зависимость?)
    private static Map<Coordinates, String> getFilteredMap(Coordinates coordinates, UserColor color) {
        Map<Coordinates, String> rawPoints = GameManager.getCurrentGame().getBoard().getBoard();
        return rawPoints.entrySet().stream()
                .filter(entry -> entry.getValue().equals(color.getColor()) && !entry.getKey().equals(coordinates))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // todo
    private static Coordinates findRandomEmpty() {
        return null;
    }
}
