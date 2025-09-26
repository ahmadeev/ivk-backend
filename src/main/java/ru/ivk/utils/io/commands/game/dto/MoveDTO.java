package ru.ivk.utils.io.commands.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class MoveDTO implements DTO {
    private final int x;
    private final int y;
}
