package ru.ivk.game;

import lombok.Getter;
import ru.ivk.utils.math.Coordinates;

import java.util.HashMap;
import java.util.Map;

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

    public void move(Coordinates coordinates) {

    }
}
