package ru.ivk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Board {
    private final int size;
    private final String[][] board;
}
