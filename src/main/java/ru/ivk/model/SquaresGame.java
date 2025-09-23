package ru.ivk.model;

import lombok.Getter;

@Getter
public class SquaresGame {
    private final Board board;
    private final User user1;
    private final User user2;

    public SquaresGame(Board board, User user1, User user2) {
        this.board = board;
        this.user1 = user1;
        this.user2 = user2;
    }
}
