package ru.ivk.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.ivk.game.model.Player;

@AllArgsConstructor
@Getter
public class GameManager {
    private final GameBoard gameBoard;
    private final Player player1;
    private final Player player2;
    @Setter
    private Player currentPlayer;

    public GameManager(GameBoard gameBoard, Player player1, Player player2) {
        this.gameBoard = gameBoard;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    public void switchCurrentPlayer() {
        setCurrentPlayer(currentPlayer.equals(player1) ? player2 : player1);
    }
}
