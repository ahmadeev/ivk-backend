package ru.ivk.game;

import lombok.extern.slf4j.Slf4j;
import ru.ivk.game.model.UserColor;
import ru.ivk.utils.math.Coordinates;
import ru.ivk.utils.math.PlaneMath;
import ru.ivk.utils.math.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GameEngine {
    public boolean isWin(GameBoard gameBoard, Coordinates coordinates, UserColor color) {
        Map<Coordinates, String> points = gameBoard.getMovesByColor(color);
        log.info("Отфильтрованные по цвету {} точки: {}", color.getColor(), points);
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
                log.info("Найден квадрат с координатами: ({},{}), ({},{}), ({},{}), ({},{})",
                        point11.getX(), point11.getY(),
                        point12.getX(), point12.getY(),
                        key.getX(), key.getY(),
                        coordinates.getX(), coordinates.getY()
                );
                return true;
            }
            if (points.containsKey(point21) && points.containsKey(point22)) {
                log.info("Найден квадрат с координатами: ({}{}), ({}{}), ({}{}), ({}{})",
                        point21.getX(), point21.getY(),
                        point22.getX(), point22.getY(),
                        key.getX(), key.getY(),
                        coordinates.getX(), coordinates.getY()
                );
                return true;
            }
        }
        return false;
    }

    /** Возвращает список точек (с повторами), дополняющих некоторые рамки до квадрата */
    public List<Coordinates> findFramesOfThree(GameBoard gameBoard, Coordinates p0, UserColor color) {
        Map<Coordinates, String> points = gameBoard.getMovesByColor(color);
        log.info("Отфильтрованные по цвету {} точки: {}", color.getColor(), points);
        List<Coordinates> frameMissingElements = new LinkedList<>();

        if (points.size() < 3) return frameMissingElements;

        List<Coordinates> keys = new ArrayList<>(points.keySet());

        for (int i = 0; i + 1 < keys.size(); i++) {
            if (keys.get(i).equals(p0)) continue;
            for (int j = i + 1; j < keys.size(); j++) {
                if (keys.get(j).equals(p0)) continue;
                Coordinates p1 = keys.get(i);
                Coordinates p2 = keys.get(j);
                System.out.println("p0: " + p0 + ", p1: " + p1 + ", p2: " + p2);
                Coordinates rightAngleVertex = PlaneMath.getRightAngleVertex(p0, p1, p2);
                System.out.println("right angle vertex: " + rightAngleVertex);
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
                int x = rightAngleVertex.getX() + sum.getX();
                int y = rightAngleVertex.getY() + sum.getY();
                if (x < 0 || x >= gameBoard.getSize() || y < 0 || y >= gameBoard.getSize()) continue;
                Coordinates missingPoint = new Coordinates(x, y);
                log.info("Недостающая точка: {}", missingPoint);
                frameMissingElements.add(missingPoint);
            }
        }

        return frameMissingElements;
    }
}
