package ru.ivk.cli.io.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommandResult<D> {
    private final boolean success;
    private final boolean game;
    private final D data;

    public static CommandResult<Void> success(boolean game) {
        return new CommandResult<>(true, game, null);
    }

    public static <D> CommandResult<D> success(boolean game, D data) {
        return new CommandResult<>(true, game, data);
    }

    public static <D> CommandResult<D> error(boolean game) {
        return new CommandResult<>(false, game, null);
    }
}
