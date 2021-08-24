package engine.game2D;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class Logger {
    public static final int MAX_LOGS = 16384; // 2^14
    private static final ArrayList<String> logs = new ArrayList<>();

    public enum Level {
        DEBUG(0), INFO(1), WARNING(2), ERROR(3), CRITICAL(4);
        private final int value;
        Level(int value) { this.value = value; }
        public int getValue() { return value; }
    };

    public static Level level = Level.DEBUG; // Initial log-level
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static <T> void debug(T message) {
        output(message, Level.DEBUG);
    }
    public static <T> void info(T message) {
        output(message, Level.INFO);
    }
    public static <T> void warning(T message) {
        output(message, Level.WARNING);
    }
    public static <T> void error(T message) {
        output(message, Level.ERROR);
    }
    public static <T> void critical(T message) {
        output(message, Level.CRITICAL);
    }

    private static <T> void output(T message, Level level) {
        String output = dtf.format(LocalTime.now()) + ":" + level.toString() + ": " + message.toString();
        if (level.getValue() >= Logger.level.getValue())
            System.out.println(output);

        // Remove first logs if log-history exceeds limit
        if (logs.size() >= MAX_LOGS) {
           logs.remove(0);
        }
        logs.add(output);
    }

    public static ArrayList<String> getLogs() {
        return logs;
    }
}
