package ru.ivk.cli.io.commands.exceptions;

public class InvalidNumberOfArgumentsException extends RuntimeException {
    public InvalidNumberOfArgumentsException(String message) {
        super(message);
    }

}
