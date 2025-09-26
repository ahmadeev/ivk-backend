package ru.ivk.utils.io.commands;

public interface Executable<D> {
    CommandResult<D> execute();
}
