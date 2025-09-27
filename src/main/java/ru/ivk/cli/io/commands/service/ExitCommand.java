package ru.ivk.cli.io.commands.service;

import lombok.AllArgsConstructor;
import ru.ivk.cli.io.commands.Command;
import ru.ivk.cli.io.commands.CommandResult;

@AllArgsConstructor
public class ExitCommand extends Command<Void> {
    private final String[] args;

    @Override
    public CommandResult<Void> execute() {
        System.out.println("Завершение работы");
        System.exit(0);
        return CommandResult.success(false);
    }

    @Override
    protected Void convertArgs(String[] args) {
        return null;
    }
}
