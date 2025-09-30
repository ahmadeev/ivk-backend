package ru.ivk.common.game;

import lombok.Getter;
import ru.ivk.common.game.model.UserColor;
import ru.ivk.common.math.Coordinates;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class GameBoard {
    private final int size;
    private final Map<Coordinates, String> board;
    private int emptySquaresCount;
    private final Random random;

    public GameBoard(int size) {
        this.size = size;
        this.board = new HashMap<>();
        this.emptySquaresCount = size * size;
        this.random = new Random();
    }

    public GameBoard(int size, Map<Coordinates, String> board) {
        this.size = size;
        this.board = board;
        this.emptySquaresCount = size * size - board.size();
        this.random = new Random();
    }

    public boolean isFree(Coordinates coordinates) {
        return !board.containsKey(coordinates);
    }

    public void move(Coordinates coordinates, String color) {
        this.board.put(coordinates, color);
        this.emptySquaresCount--;
    }

    public Coordinates findFree() {
        if (this.emptySquaresCount < this.size * this.size * 0.3) {
            return findFirstFree();
        } else {
            return findRandomFree();
        }
    }

    public Coordinates findFirstFree() {
        Set<Coordinates> occupied = new HashSet<>(this.board.keySet());
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                Coordinates c = new Coordinates(i, j);
                if (!occupied.contains(c)) {
                    return c;
                }
            }
        }
        return null;
    }

    public Coordinates findRandomFree() {
        int maxAttempts = 20;
        for (int i = 0; i < maxAttempts; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            Coordinates coordinates = new Coordinates(x, y);
            if (isFree(coordinates)) return coordinates;
        }
        return findFirstFree();
    }

    public Map<Coordinates, String> getMovesByColor(UserColor color) {
        return this.board.entrySet().stream()
                .filter(entry -> entry.getValue().equals(color.getColor()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
