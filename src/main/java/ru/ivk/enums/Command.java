package ru.ivk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Command {
    GAME("GAME"),
    MOVE("MOVE"),
    EXIT("EXIT"),
    HELP("HELP");
    private final String command;
}
