package utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadUtils {
    @Contract("_ -> new")
    @NotNull
    public static ScheduledThreadPoolExecutor getDaemonScheduledThreadPoolExecutor(int poolSize) {
        return new ScheduledThreadPoolExecutor(poolSize, runnable -> {
            final Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }
}

