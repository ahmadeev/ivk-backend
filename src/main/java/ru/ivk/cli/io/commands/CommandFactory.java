package ru.ivk.cli.io.commands;

import ru.ivk.cli.io.commands.game.GameCommand;
import ru.ivk.cli.io.commands.game.MoveCommand;
import ru.ivk.cli.io.commands.service.ExitCommand;
import ru.ivk.cli.io.commands.service.HelpCommand;

public class CommandFactory {
    public static Command<?> getCommand(InputCommand inputCommand, String[] args) {
        switch (inputCommand) {
            case GAME:
                return new GameCommand(args);
            case MOVE:
                return new MoveCommand(args);
            case EXIT:
                return new ExitCommand(args);
            case HELP:
                return new HelpCommand(args);
            default:
                return null;
        }
    }
}
