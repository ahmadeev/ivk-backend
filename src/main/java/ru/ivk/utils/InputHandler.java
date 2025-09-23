package ru.ivk.utils;

import ru.ivk.enums.Command;
import ru.ivk.model.UserInput;

import java.util.Arrays;
import java.util.Scanner;

public class InputHandler {
    private final static Scanner scanner = new Scanner(System.in);

    public UserInput awaitUserInput() throws Exception {
        System.out.print("> ");

        String input = scanner.nextLine();
        String[] tokens = input.split("\\s++", 2);

        Command cmd;
        try {
            cmd = Command.valueOf(tokens[0]);
        } catch (Exception e) {
            throw new Exception("Введенная команда не существует!");
        }

        String[] args = new String[0];
        if (tokens.length > 1) {
            args = Arrays.stream(tokens[1].split(",")).map(String::trim).toArray(String[]::new);
        }

        switch (cmd) {
            case GAME:
                // GAME N, U1, U2
                // N > 2; U -> 'TYPE C'
                if (args.length != 3) throw new Exception("Неверное число аргументов!");
                return new UserInput(cmd, args);
            case MOVE:
                // MOVE X, Y
                if (args.length != 2) throw new Exception("Неверное число аргументов!");
                return new UserInput(cmd, args);
            case EXIT:
                if (args.length != 0) throw new Exception("Неверное число аргументов!");
                System.exit(130);
                return new UserInput(cmd, args);
            case HELP:
                if (args.length != 0) throw new Exception("Неверное число аргументов!");
                return new UserInput(cmd, args);
            default:
                throw new Exception("Что-то пошло не так... Введенная команда не существует!");
        }
    }
}
