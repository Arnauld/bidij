package bdi.junit;

import org.junit.runners.model.RunnerScheduler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ParallelScheduler implements RunnerScheduler {

    private final ParallelOptions options;
    private final ExecutorService executor;
    private final OutputStream outputStream;

    public ParallelScheduler(ParallelOptions options,
                             ExecutorService executor,
                             OutputStream outputStream) {
        this.options = options;
        this.executor = executor;
        this.outputStream = outputStream;
    }

    public void schedule(Runnable childStatement) {
        executor.submit(childStatement);
    }

    public void finished() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(options.terminationTimeout(), options.terminationTimeoutUnit()))
                writeln(outputStream, "scheduler shutdown timed out before tests completed, you may have executors hanging around...");
        } catch (InterruptedException e) {
            // ignore?
        }
    }

    private void writeln(OutputStream stream, String string) {
        try {
            stream.write(string.getBytes());
            stream.write(System.getProperty("line.separator").getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
