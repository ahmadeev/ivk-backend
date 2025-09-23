package ru.ivk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ivk.enums.UserColor;
import ru.ivk.enums.UserType;

@AllArgsConstructor
@Getter
public class User {
    private final UserType userType;
    private final UserColor userColor;

    private void move(Board board, Coordinates coordinates) throws Exception {
        int x = coordinates.getX();
        int y = coordinates.getY();
        if (x >= board.getSize() || y >= board.getSize()) {
            throw new Exception("Переданы невалидные координаты!");
        }
        board.getBoard()[x][y] = this.userColor.getColor();
    }

    public void makeMove(Board board, Coordinates coordinates) throws Exception {
        if (this.userType == UserType.COMP) {
            // считаем лучшие координаты и записываем в coordinates
        }
        if (coordinates == null) throw new Exception("Что-то пошло не так... Координаты не были переданы.");
        this.move(board, coordinates);
    }
}
