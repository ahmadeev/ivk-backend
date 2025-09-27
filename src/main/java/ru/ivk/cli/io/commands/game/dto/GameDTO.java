package ru.ivk.cli.io.commands.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class GameDTO implements DTO {
    private final int n;
    private final String player1Type;
    private final String player1Color;
    private final String player2Type;
    private final String player2Color;
}
