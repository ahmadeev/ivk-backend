package ru.ivk.cli.io.commands;

public interface Executable<D> {
    CommandResult<D> execute();
}
