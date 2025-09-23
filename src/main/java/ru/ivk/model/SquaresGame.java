package ru.ivk.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // лучше бы, наверное, private
public class SquaresGame {
    private final Board board;
    private final User user1;
    private final User user2;
    private User nextToMove;

    public SquaresGame(Board board, User user1, User user2) {
        this.board = board;
        this.user1 = user1;
        this.user2 = user2;
        this.nextToMove = user1; // по умолчанию первый записанный игрок ходит первым
    }

    public void switchNextToMove() {
        setNextToMove(nextToMove.equals(user1) ? user2 : user1);
    }
}
