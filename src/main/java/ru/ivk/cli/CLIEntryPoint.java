package ru.ivk.cli;

import ru.ivk.common.game.GameEngine;
import ru.ivk.cli.io.IOProcessor;
import ru.ivk.cli.io.commands.game.dto.DTO;

import java.util.concurrent.LinkedBlockingQueue;

public class CLIEntryPoint {
    public static void main(String[] args) {
        LinkedBlockingQueue<DTO> sharedQueue = new LinkedBlockingQueue<>();

        GameLoop loop = new GameLoop(sharedQueue, new GameEngine());
        loop.run();

        IOProcessor io = new IOProcessor(sharedQueue);
        io.run();
    }
}
