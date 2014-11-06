package bdi.glue.proc.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ProcWorld {

    private final Stack<Proc> processStack = new Stack<>();
    private String currentDir;
    private String outputDir;
    //
    private long defaultTerminationTimeout = 2;
    private TimeUnit defaultTerminationTimeoutUnit = TimeUnit.SECONDS;


    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void pushProcess(Proc proc) {
        processStack.push(proc);
    }

    public boolean hasProcess() {
        return !processStack.isEmpty();
    }

    public Proc peekProcess() {
        if (hasProcess()) {
            return processStack.peek();
        } else {
            throw new ProcException("No process registered");
        }
    }

    public String getOutputDir() {
        if (outputDir == null) {
            Path currentRelativePath = Paths.get("out");
            outputDir = currentRelativePath.toAbsolutePath().toString();
        }
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public long getDefaultTerminationTimeout() {
        return defaultTerminationTimeout;
    }

    public void setDefaultTerminationTimeout(long terminationTimeout) {
        this.defaultTerminationTimeout = terminationTimeout;
    }

    public TimeUnit getDefaultTerminationTimeoutUnit() {
        return defaultTerminationTimeoutUnit;
    }

    public void setDefaultTerminationTimeoutUnit(TimeUnit terminationTimeoutUnit) {
        this.defaultTerminationTimeoutUnit = terminationTimeoutUnit;
    }

    public void waitTermination(Proc proc) throws TimeoutException, InterruptedException {
        waitForTermination(proc, defaultTerminationTimeout, defaultTerminationTimeoutUnit);
    }

    public void waitForTermination(Proc proc, long terminationTimeout, TimeUnit terminationTimeoutUnit) throws TimeoutException, InterruptedException {
        proc.waitForTermination(terminationTimeout, terminationTimeoutUnit);
    }

    public void waitLastProcessTermination() throws TimeoutException, InterruptedException {
        waitForTermination(peekProcess(), defaultTerminationTimeout, defaultTerminationTimeoutUnit);
    }

    public void waitLastProcessTermination(int timeout, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException {
        waitForTermination(peekProcess(), timeout, timeoutUnit);
    }
}
