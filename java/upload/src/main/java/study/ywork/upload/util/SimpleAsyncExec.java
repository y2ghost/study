package study.ywork.upload.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimpleAsyncExec {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(5);
    private static final Logger LOG = LoggerFactory.getLogger(SimpleAsyncExec.class);

    public static <T> T exec(Callable<T> callable) {
        T t = null;
        Future<T> future = EXECUTOR.submit(callable);

        try {
            t = future.get();
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOG.error(e.getMessage(), e);
        }

        return t;
    }

    public static void exec(Runnable run) {
        EXECUTOR.submit(run);
    }

    private SimpleAsyncExec() {
    }
}