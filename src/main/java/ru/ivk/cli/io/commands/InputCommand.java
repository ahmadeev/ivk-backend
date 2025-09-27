package ru.ivk.cli.io.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InputCommand {
    GAME("GAME", 3),
    MOVE("MOVE", 2),
    EXIT("EXIT", 0),
    HELP("HELP", 0);
    private final String command;
    private final int argsAmount;
}
