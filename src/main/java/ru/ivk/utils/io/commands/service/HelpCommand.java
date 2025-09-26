package ru.ivk.utils.io.commands.service;

import lombok.AllArgsConstructor;
import ru.ivk.utils.io.commands.Command;
import ru.ivk.utils.io.commands.CommandResult;
import ru.ivk.utils.io.commands.InputCommand;

import java.util.Arrays;

@AllArgsConstructor
public class HelpCommand extends Command<Void> {
    private final String[] args;

    @Override
    public CommandResult<Void> execute() {
        System.out.println("Список доступных команд: " + Arrays.toString(InputCommand.values()));
        return CommandResult.success(false);
    }

    @Override
    protected Void convertArgs(String[] args) {
        return null;
    }
}
