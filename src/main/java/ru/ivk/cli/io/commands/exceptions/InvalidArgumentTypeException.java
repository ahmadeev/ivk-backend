package ru.ivk.cli.io.commands.exceptions;

public class InvalidArgumentTypeException extends RuntimeException {
    public InvalidArgumentTypeException(String message) {
        super(message);
    }
}
