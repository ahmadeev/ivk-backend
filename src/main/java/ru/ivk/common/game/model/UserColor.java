package ru.ivk.common.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserColor {
    WHITE("W"),
    BLACK("B");
    private final String color;

    public static UserColor getOtherColor(UserColor color) {
        return color.equals(WHITE) ? BLACK : WHITE;
    }
}
