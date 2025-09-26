package ru.ivk.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserColor {
    WHITE("W"),
    BLACK("B");
    private final String color;
}
