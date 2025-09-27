package ru.ivk.utils.io;

import lombok.extern.slf4j.Slf4j;
import ru.ivk.utils.io.commands.Command;
import ru.ivk.utils.io.commands.CommandFactory;
import ru.ivk.utils.io.commands.CommandResult;
import ru.ivk.utils.io.commands.InputCommand;
import ru.ivk.utils.io.commands.exceptions.CommandNotFoundException;
import ru.ivk.utils.io.commands.exceptions.InvalidArgumentTypeException;
import ru.ivk.utils.io.commands.exceptions.InvalidNumberOfArgumentsException;
import ru.ivk.utils.io.commands.game.dto.DTO;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

@Slf4j
public class IOProcessor {
    private final LinkedBlockingQueue<DTO> queue;

    private final static Scanner scanner = new Scanner(System.in);

    private final String integerRegex = "^\\d+$";
    private final Pattern integerPattern = Pattern.compile(integerRegex);

    private final String userConfigRegex = "^(USER|COMP)\\s++(BLACK|WHITE)$";
    private final Pattern userConfigPattern = Pattern.compile(userConfigRegex);

    public IOProcessor(LinkedBlockingQueue<DTO> queue) {
        this.queue = queue;
    }

    public void run() {
        Runnable r = () -> {
            while (true) {
                try {
                    CommandResult<?> commandResult = awaitUserInput().execute();
                    if (commandResult.isGame()) {
                        boolean wasAdded = queue.offer((DTO) commandResult.getData());
                        if (!wasAdded) throw new RuntimeException("Не удалось выполнить команду");
                        log.debug("Отправка {}: {}", commandResult.getData().getClass().getSimpleName(), commandResult.getData());
                    }
                } catch (CommandNotFoundException | InvalidNumberOfArgumentsException | InvalidArgumentTypeException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public Command<?> awaitUserInput() {
        System.out.print("> ");

        String input = scanner.nextLine();
        String[] tokens = input.split("\\s++", 2);

        InputCommand cmd;
        try {
            cmd = InputCommand.valueOf(tokens[0]);
        } catch (Exception e) {
            throw new CommandNotFoundException("Введенная команда не существует!\nВведите 'HELP', чтобы увидеть список доступных команд");
        }

        String[] args = new String[0];
        if (tokens.length > 1) {
            args = Arrays.stream(tokens[1].split(",")).map(String::trim).toArray(String[]::new);
        }

        switch (cmd) {
            case GAME:
                // GAME N, U1, U2
                // N > 2; U -> 'TYPE C'
                if (args.length != InputCommand.GAME.getArgsAmount()) throw new InvalidNumberOfArgumentsException(String.format("Неверное число аргументов! (Ожидается: %d; получено: %d)", InputCommand.GAME.getArgsAmount(), args.length));
                if (!integerPattern.matcher(args[0]).matches()) throw new InvalidArgumentTypeException("Неверный тип аргумента № 1! (Ожидается целочисленный тип)");
                if (Integer.parseInt(args[0]) <= 2) throw new InvalidArgumentTypeException("Неверный тип аргумента № 1! (Ожидается целое число, большее двух)");
                if (!userConfigPattern.matcher(args[1]).matches()) throw new InvalidArgumentTypeException("Неверный тип аргумента № 2! (Ожидается строка в формате 'TYPE COLOR')");
                if (!userConfigPattern.matcher(args[2]).matches()) throw new InvalidArgumentTypeException("Неверный тип аргумента № 3! (Ожидается строка в формате 'TYPE COLOR')");
                return CommandFactory.getCommand(cmd, args);
            case MOVE:
                // MOVE X, Y
                if (args.length != InputCommand.MOVE.getArgsAmount()) throw new InvalidNumberOfArgumentsException(String.format("Неверное число аргументов! (Ожидается: %d; получено: %d)", InputCommand.MOVE.getArgsAmount(), args.length));
                if (!integerPattern.matcher(args[0]).matches()) throw new InvalidArgumentTypeException("Неверный тип аргумента № 1! (Ожидается целочисленный тип)");
                if (!integerPattern.matcher(args[1]).matches()) throw new InvalidArgumentTypeException("Неверный тип аргумента № 2! (Ожидается целочисленный тип)");
                return CommandFactory.getCommand(cmd, args);
            case EXIT:
            case HELP:
                if (args.length != InputCommand.HELP.getArgsAmount()) throw new InvalidNumberOfArgumentsException(String.format("Неверное число аргументов! (Ожидается: %d; получено: %d)", InputCommand.HELP.getArgsAmount(), args.length));
                return CommandFactory.getCommand(cmd, args);
            default:
                throw new CommandNotFoundException("Что-то пошло не так... Введенная команда не существует!");
        }
    }
}
