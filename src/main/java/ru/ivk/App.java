package ru.ivk;

import org.slf4j.simple.SimpleLogger;
import ru.ivk.game.GameEngine;
import ru.ivk.game.GameLoop;
import ru.ivk.utils.io.IOProcessor;
import ru.ivk.utils.io.commands.game.dto.DTO;

import java.util.concurrent.LinkedBlockingQueue;

public class App {
    static {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "HH:mm:ss.SSS");
        System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_LOG_NAME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
    }

    public static void main( String[] args ) {
        LinkedBlockingQueue<DTO> sharedQueue = new LinkedBlockingQueue<>();

        GameLoop loop = new GameLoop(sharedQueue, new GameEngine());
        loop.run();

        IOProcessor io = new IOProcessor(sharedQueue);
        io.run();
    }
}
