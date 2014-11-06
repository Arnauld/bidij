package bdi.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ParallelRunner extends BlockJUnit4ClassRunner {

    public ParallelRunner(Class<?> type) throws InitializationError {
        super(type);
        ParallelOptions options = findOptions(type);
        setScheduler(
                new ParallelScheduler(options,
                        createExecutor(options),
                        System.err));
    }

    private static ExecutorService createExecutor(ParallelOptions options) {
        if (options != null && options.concurrency() > 0)
            return newFixedThreadPool(options.concurrency(), new ConcurrentTestRunnerThreadFactory());
        return newCachedThreadPool(new ConcurrentTestRunnerThreadFactory());
    }

    private static ParallelOptions findOptions(Class<?> type) {
        ParallelOptions concurrent = null;
        while (concurrent == null && type.getSuperclass() != null) {
            concurrent = type.getAnnotation(ParallelOptions.class);
            type = type.getSuperclass();
        }
        return concurrent;
    }

    private static class ConcurrentTestRunnerThreadFactory implements ThreadFactory {
        private final AtomicLong count = new AtomicLong();

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, ParallelRunner.class.getSimpleName() + "-Thread-" + count.getAndIncrement());
        }
    }
}
