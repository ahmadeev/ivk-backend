package ru.ivk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserColor {
    BLACK("B"),
    WHITE("W");
    private final String color;
}