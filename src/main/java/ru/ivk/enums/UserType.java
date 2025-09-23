package ru.ivk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    USER("user"),
    COMP("comp");
    private final String type;
}
