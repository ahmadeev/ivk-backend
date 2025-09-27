package ru.ivk;

import ru.ivk.game.GameEngine;
import ru.ivk.game.GameLoop;
import ru.ivk.utils.io.IOProcessor;
import ru.ivk.utils.io.commands.game.dto.DTO;

import java.util.concurrent.LinkedBlockingQueue;

public class App {
    public static void main( String[] args ) {
        LinkedBlockingQueue<DTO> sharedQueue = new LinkedBlockingQueue<>();

        GameLoop loop = new GameLoop(sharedQueue, new GameEngine());
        loop.run();

        IOProcessor io = new IOProcessor(sharedQueue);
        io.run();
    }
}
