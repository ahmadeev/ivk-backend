package ru.ivk.utils;

import ru.ivk.enums.UserColor;
import ru.ivk.enums.UserType;
import ru.ivk.model.Board;
import ru.ivk.model.SquaresGame;
import ru.ivk.model.User;

public class GameManager {
    private static SquaresGame currentGame;

    public static void startNewGame(int boardSize, String user1Type, String user1Color, String user2Type, String user2Color) {
        Board board = new Board(boardSize);
        User user1 = new User(UserType.valueOf(user1Type), UserColor.valueOf(user1Color));
        User user2 = new User(UserType.valueOf(user2Type), UserColor.valueOf(user2Color));
        currentGame = new SquaresGame(board, user1, user2);
    }

    public static SquaresGame getCurrentGame() throws Exception {
        if (currentGame == null) {
            throw new Exception("Игра не была начата!");
        }
        return currentGame;
    }

    public static boolean hasActiveGame() {
        return currentGame != null;
    }

    public static void resetCurrentGame() {
        currentGame = null;
    }
}
