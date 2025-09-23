package ru.ivk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ivk.enums.CommandName;

@AllArgsConstructor
@Getter
public class UserInput {
    private final CommandName command;
    private final String[] args;
}
