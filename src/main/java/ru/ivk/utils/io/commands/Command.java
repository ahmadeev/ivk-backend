package ru.ivk.utils.io.commands;

public abstract class Command<D> implements Executable<D> {
    protected abstract D convertArgs(String[] args);
}
