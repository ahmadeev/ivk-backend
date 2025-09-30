package ru.ivk.common.game;

import lombok.extern.slf4j.Slf4j;
import ru.ivk.common.Utilities;
import ru.ivk.common.game.model.UserColor;
import ru.ivk.common.math.Coordinates;
import ru.ivk.common.math.PlaneMath;
import ru.ivk.common.math.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GameEngine {
    public boolean isWin(GameBoard gameBoard, Coordinates coordinates, UserColor color) {
        Map<Coordinates, String> points = gameBoard.getMovesByColor(color);
        log.debug("Отфильтрованные по цвету {} точки: {}", color.getColor(), points);
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
                log.debug("Найден квадрат с координатами: ({},{}), ({},{}), ({},{}), ({},{})",
                        point11.getX(), point11.getY(),
                        point12.getX(), point12.getY(),
                        key.getX(), key.getY(),
                        coordinates.getX(), coordinates.getY()
                );
                return true;
            }
            if (points.containsKey(point21) && points.containsKey(point22)) {
                log.debug("Найден квадрат с координатами: ({}{}), ({}{}), ({}{}), ({}{})",
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

    public boolean isWin(GameBoard gameBoard, UserColor color) {
        Map<Coordinates, String> points = gameBoard.getMovesByColor(color);
        log.debug("Отфильтрованные по цвету {} точки: {}", color.getColor(), points);
        if (points.size() < 4) return false;

        for (Coordinates key : points.keySet()) {
            if (isWin(gameBoard, key, color)) return true;
        }
        return false;
    }

    // todo: на больших досках не находит победу, потому что не складывается рамка из трех. идет глупый перебор
    // todo: нужно искать решения и для двух точек
    /** Возвращает список точек (с повторами), дополняющих некоторые рамки до квадрата определенного цвета */
    public List<Coordinates> findFramesOfThree(GameBoard gameBoard, Coordinates p0, UserColor color) {
        Map<Coordinates, String> points = gameBoard.getMovesByColor(color);
        log.debug("Отфильтрованные по цвету {} точки: {}", color.getColor(), points);
        List<Coordinates> frameMissingElements = new LinkedList<>();

        if (points.size() < 3) return frameMissingElements;

        List<Coordinates> keys = new ArrayList<>(points.keySet());

        for (int i = 0; i + 1 < keys.size(); i++) {
            if (keys.get(i).equals(p0)) continue;
            for (int j = i + 1; j < keys.size(); j++) {
                if (keys.get(j).equals(p0)) continue;
                Coordinates p1 = keys.get(i);
                Coordinates p2 = keys.get(j);
                Coordinates rightAngleVertex = PlaneMath.getRightAngleVertex(p0, p1, p2);
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

                // fix: раньше брали и прямоугольники
                if (v1.getLength() != v2.getLength()) continue;

                Vector sum = Vector.sum(v1, v2);
                int x = rightAngleVertex.getX() + sum.getX();
                int y = rightAngleVertex.getY() + sum.getY();
                if (x < 0 || x >= gameBoard.getSize() || y < 0 || y >= gameBoard.getSize()) continue;
                if (gameBoard.getBoard().containsKey(new Coordinates(x, y))) continue;
                Coordinates missingPoint = new Coordinates(x, y);
                log.debug("Недостающая точка: {} для векторов {} и {}", missingPoint, v1, v2);
                frameMissingElements.add(missingPoint);
            }
        }

        return frameMissingElements;
    }

    /** Возвращает список точек (с повторами), дополняющих некоторые рамки до квадрата определенного цвета */
    public List<Coordinates> findFramesOfThree(GameBoard gameBoard, UserColor color) {
        Map<Coordinates, String> points = gameBoard.getMovesByColor(color);
        log.debug("Отфильтрованные по цвету {} точки: {}", color.getColor(), points);
        List<Coordinates> frameMissingElements = new LinkedList<>();

        if (points.size() < 3) return frameMissingElements;

        for (Coordinates point : points.keySet()) {
            List<Coordinates> pm = this.findFramesOfThree(gameBoard, point, color);
            frameMissingElements.addAll(pm);
        }

        return frameMissingElements;
    }
    
    public Coordinates findBestMove(GameBoard gameBoard, UserColor color) {
        // todo: проверка на возможность сделать ход
        Coordinates move = null;
        UserColor otherPlayerColor = UserColor.getOtherColor(color);
        // сначала проверяем потенциальные угрозы
        List<Coordinates> threats = this.findFramesOfThree(gameBoard, otherPlayerColor);
        move = Utilities.findMostFrequent(threats);
        // если угроз нет, проверяем выигрышные ходы
        if (move == null) {
            List<Coordinates> moves = this.findFramesOfThree(gameBoard, color);
            move = Utilities.findMostFrequent(moves);
        }
        // если выигрышных ходов нет, берем случайный ход
        if (move == null) move = gameBoard.findFirstEmpty();
        return move;
    }
}
