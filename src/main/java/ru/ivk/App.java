package ru.ivk;

import org.slf4j.simple.SimpleLogger;
import ru.ivk.cli.CLIEntryPoint;
import ru.ivk.web.WebEntryPoint;

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
        if (args.length > 1) throw new RuntimeException("Неверное число аргументов запуска");
        if (args[0].equals("--cli")) CLIEntryPoint.main(args);
        else if (args[0].equals("--web")) WebEntryPoint.main(args);
        else throw new RuntimeException("Неверный аргумент");
    }
}
