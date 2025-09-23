package ru.ivk.utils;

import ru.ivk.enums.CommandName;
import ru.ivk.enums.UserType;
import ru.ivk.model.Coordinates;
import ru.ivk.model.UserInput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class InputHandler {
    private final static Scanner scanner = new Scanner(System.in);

    private final static Map<CommandName, Consumer<String[]>> commands = new HashMap<>();

    private final String integerRegex = "^\\d+$";
    private final Pattern integerPattern = Pattern.compile(integerRegex);

    private final String userConfigRegex = "^(USER|COMP)\\s++(BLACK|WHITE)$";
    private final Pattern userConfigPattern = Pattern.compile(userConfigRegex);

    public InputHandler() {
        commands.put(CommandName.GAME, (args) -> {
            if (GameManager.hasActiveGame()) throw new RuntimeException("Создание новой игры невозможно! Уже существует активная игра");
            // wa: придумать что-нибудь покрасивее
            GameManager.startNewGame(
                    Integer.parseInt(args[0]),
                    args[1].split("\\s++")[0],
                    args[1].split("\\s++")[1],
                    args[2].split("\\s++")[0],
                    args[2].split("\\s++")[1]
            );
            System.out.println("Новая игра начата");
            // ------ wa: придумать красивое решение для ходов компьютера
            try {
                if (GameManager.getCurrentGame().getNextToMove().getUserType().equals(UserType.COMP)) {
                    commands.get(CommandName.MOVE).accept(new String[]{"0", "0"});
                    if (GameManager.getCurrentGame().getBoard().getEmptySquaresCount() == 0) {
                        GameManager.resetCurrentGame();
                        System.out.println("Игра окончена. Ничья.");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Что-то пошло не так... " + e.getMessage());
            }
            // ------
        });
        commands.put(CommandName.MOVE, (args) -> {
            if (!GameManager.hasActiveGame()) throw new RuntimeException("Невозможно выполнить ход: игра не была создана!");
            // todo: валидировать при парсинге
            Coordinates coordinates = new Coordinates(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1])
            );
            String nextToMoveLabel;
            try {
                nextToMoveLabel = GameManager.getCurrentGame().getNextToMove().getUserColor().getColor();
                GameManager.getCurrentGame().getNextToMove().makeMove(coordinates);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            System.out.printf("%s (%d, %d)%n", nextToMoveLabel, coordinates.getX(), coordinates.getY());
            // ------ wa: придумать красивое решение для ходов компьютера
            try {
                if (GameManager.getCurrentGame().getNextToMove().getUserType().equals(UserType.COMP)) {
                    commands.get(CommandName.MOVE).accept(new String[]{"0", "0"});
                    if (GameManager.getCurrentGame().getBoard().getEmptySquaresCount() == 0) {
                        GameManager.resetCurrentGame();
                        System.out.println("Игра окончена. Ничья.");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Что-то пошло не так... " + e.getMessage());
            }
            // ------
        });
        commands.put(CommandName.EXIT, (args) -> {
            System.exit(130);
            System.out.println("Завершение работы");
        });
        commands.put(CommandName.HELP, (args) -> System.out.println("Список доступных команд: " + Arrays.toString(CommandName.values())));
    }

    public UserInput awaitUserInput() throws Exception {
        System.out.print("> ");

        String input = scanner.nextLine();
        String[] tokens = input.split("\\s++", 2);

        CommandName cmd;
        try {
            cmd = CommandName.valueOf(tokens[0]);
        } catch (Exception e) {
            throw new Exception("Введенная команда не существует!\nВведите 'HELP', чтобы увидеть список доступных команд");
        }

        String[] args = new String[0];
        if (tokens.length > 1) {
            args = Arrays.stream(tokens[1].split("\\s++,\\s++")).map(String::trim).toArray(String[]::new);
        }

        switch (cmd) {
            case GAME:
                // GAME N, U1, U2
                // N > 2; U -> 'TYPE C'
                if (args.length != 3) throw new Exception("Неверное число аргументов! (Ожидается 3 аргумента)");
                if (!integerPattern.matcher(args[0]).matches()) throw new Exception("Неверный тип аргумента № 1! (Ожидается целочисленный тип)");
                if (Integer.parseInt(args[0]) <= 2) throw new Exception("Неверный тип аргумента № 1! (Ожидается целое число, большее двух)");
                if (!userConfigPattern.matcher(args[1]).matches()) throw new Exception("Неверный тип аргумента № 2! (Ожидается строка в формате 'TYPE COLOR')");
                if (!userConfigPattern.matcher(args[2]).matches()) throw new Exception("Неверный тип аргумента № 3! (Ожидается строка в формате 'TYPE COLOR')");
                return new UserInput(cmd, args);
            case MOVE:
                // MOVE X, Y
                if (args.length != 2) throw new Exception("Неверное число аргументов! (Ожидается 2 аргумента)");
                if (!integerPattern.matcher(args[0]).matches()) throw new Exception("Неверный тип аргумента № 1! (Ожидается целочисленный тип)");
                if (!integerPattern.matcher(args[1]).matches()) throw new Exception("Неверный тип аргумента № 2! (Ожидается целочисленный тип)");
                return new UserInput(cmd, args);
            case EXIT:
            case HELP:
                if (args.length != 0) throw new Exception("Неверное число аргументов! (Ожидается 0 аргументов)");
                return new UserInput(cmd, args);
            default:
                throw new Exception("Что-то пошло не так... Введенная команда не существует!");
        }
    }

    public void handleUserInput(UserInput userInput) throws Exception {
        CommandName cmd = userInput.getCommand();
        String[] args = userInput.getArgs();

        Consumer<String[]> command = commands.get(cmd);
        if (command == null) throw new Exception("Что-то пошло не так... Введенная команда не найдена!");
        command.accept(args);
        if (GameManager.hasActiveGame() && GameManager.getCurrentGame().getBoard().getEmptySquaresCount() == 0) {
            GameManager.resetCurrentGame();
            System.out.println("Игра окончена. Ничья.");
        }
    }
}
