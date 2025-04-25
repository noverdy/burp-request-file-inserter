package com.yesver.burpfileinserter.utils;

import burp.api.montoya.MontoyaApi;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final MontoyaApi api;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Logger(MontoyaApi api) {
        this.api = api;
    }

    public void info(String message, Object... args) {
        log("INFO", message, args);
    }

    public void warn(String message, Object... args) {
        log("WARN", message, args);
    }

    public void error(String message, Object... args) {
        log("ERROR", message, args);
    }

    public void error(Throwable t) {
        api.logging().logToError(formatMessage("ERROR", t.toString()));
    }

    private void log(String level, String message, Object... args) {
        String formatted = String.format(message, args);
        String output = formatMessage(level, formatted);
        if ("ERROR".equals(level)) {
            api.logging().logToError(output);
        } else {
            api.logging().logToOutput(output);
        }
    }

    private String formatMessage(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        return String.format("[%s] [%s] %s", timestamp, level, message);
    }
}
