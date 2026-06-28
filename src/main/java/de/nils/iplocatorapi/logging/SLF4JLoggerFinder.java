package de.nils.iplocatorapi.logging;

import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class SLF4JLoggerFinder extends System.LoggerFinder {
    @Override
    public System.Logger getLogger(String name, Module module) {
        return new LoggerBridge(name);
    }

    private class LoggerBridge implements System.Logger {
        private String name;
        private org.slf4j.Logger log;

        public LoggerBridge(String name) {
            this.name = name;
            log = LoggerFactory.getLogger(name);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isLoggable(Level level) {
            switch(level) {
                case ALL -> {
                    return true;
                }
                case TRACE -> {
                    return log.isTraceEnabled();
                }
                case DEBUG -> {
                    return log.isDebugEnabled();
                }
                case INFO -> {
                    return log.isInfoEnabled();
                }
                case WARNING -> {
                    return log.isWarnEnabled();
                }
                case ERROR -> {
                    return log.isErrorEnabled();
                }
                default -> {
                    return false;
                }
            }
        }

        @Override
        public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
            switch(level) {
                case TRACE -> log.trace(msg, thrown);
                case DEBUG -> log.debug(msg, thrown);
                case INFO -> log.info(msg, thrown);
                case WARNING -> log.warn(msg, thrown);
                case ERROR -> log.error(msg, thrown);
            }
        }

        @Override
        public void log(Level level, ResourceBundle bundle, String format, Object... params) {
            switch(level) {
                case TRACE -> log.trace(format, params);
                case DEBUG -> log.debug(format, params);
                case INFO -> log.info(format, params);
                case WARNING -> log.warn(format, params);
                case ERROR -> log.error(format, params);
            }
        }
    }
}
