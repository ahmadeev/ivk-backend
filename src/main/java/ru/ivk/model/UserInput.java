package ru.ivk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ivk.enums.Command;

@AllArgsConstructor
@Getter
public class UserInput {
    private final Command command;
    private final String[] args;
}
