package ru.ivk.model;

import lombok.Getter;
import ru.ivk.utils.GameManager;

@Getter
public class Board {
    private final int size;
    private final String[][] board;
    private int emptySquaresCount;

    public Board(int size) {
        this.size = size;
        this.board = new String[size][size];
        this.emptySquaresCount = size * size;
    }

    public boolean isFree(Coordinates coordinates) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        return this.board[x][y] == null;
    }

    public void claim(User user, Coordinates coordinates) throws Exception {
        if (!this.isFree(coordinates)) throw new Exception("Выбранное поле уже занято!");
        int x = coordinates.getX();
        int y = coordinates.getY();
        if (x >= this.getSize() || y >= this.getSize()) {
            throw new Exception("Переданы невалидные координаты!");
        }
        this.board[x][y] = user.getUserColor().getColor();

        emptySquaresCount--;
        GameManager.getCurrentGame().switchNextToMove();
    }
}
