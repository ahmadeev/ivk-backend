package ru.ivk.common.game;

import lombok.Getter;
import ru.ivk.common.game.model.UserColor;
import ru.ivk.common.math.Coordinates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class GameBoard {
    private final int size;
    private final Map<Coordinates, String> board;
    private int emptySquaresCount;

    public GameBoard(int size) {
        this.size = size;
        this.board = new HashMap<>();
        this.emptySquaresCount = size * size;
    }

    public boolean isFree(Coordinates coordinates) {
        return !board.containsKey(coordinates);
    }

    public void move(Coordinates coordinates, String color) {
        this.board.put(coordinates, color);
        this.emptySquaresCount--;
    }

    public Coordinates findRandomEmpty() {
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

    public Map<Coordinates, String> getMovesByColor(UserColor color) {
        return this.board.entrySet().stream()
                .filter(entry -> entry.getValue().equals(color.getColor()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
