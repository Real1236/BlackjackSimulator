package com.arthur.blackjack.config;

import java.util.logging.*;

public class LoggerConfig {
    public static void configure(Logger logger) {
//        logger.setUseParentHandlers(false);
//        ConsoleHandler handler = new ConsoleHandler();
//        handler.setFormatter(new SimpleFormatter());
//        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tT] %2$s %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        lr.getMillis(),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        logger.addHandler(handler);
    }
}

