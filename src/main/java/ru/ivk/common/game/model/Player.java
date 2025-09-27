package ru.ivk.common.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Player {
    private final UserType type;
    private final UserColor color;
}
