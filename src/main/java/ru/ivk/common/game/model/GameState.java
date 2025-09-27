package ru.ivk.common.game.model;

public enum GameState {
    IDLE,
    WAITING_FOR_PLAYER_MOVE,
    COMPUTING_PLAYERS_MOVE,
    CHECKING_CONDITIONS,
    END
}
