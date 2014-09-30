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

    private Stack<Proc> processStack = new Stack<>();
    private String currentDir;
    private String outputDir;
    //
    private long terminationTimeout = 2;
    private TimeUnit terminationTimeoutUnit = TimeUnit.SECONDS;


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
        if (hasProcess())
            return processStack.peek();
        else
            throw new ProcException("No process registered");
    }

    public String getOutputDir() {
        if (outputDir == null) {
            Path currentRelativePath = Paths.get("");
            outputDir = currentRelativePath.toAbsolutePath().toString();

        }
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public long getTerminationTimeout() {
        return terminationTimeout;
    }

    public void setTerminationTimeout(long terminationTimeout) {
        this.terminationTimeout = terminationTimeout;
    }

    public TimeUnit getTerminationTimeoutUnit() {
        return terminationTimeoutUnit;
    }

    public void setTerminationTimeoutUnit(TimeUnit terminationTimeoutUnit) {
        this.terminationTimeoutUnit = terminationTimeoutUnit;
    }

    public void waitTermination(Proc proc) throws TimeoutException, InterruptedException {
        proc.waitForTermination(terminationTimeout, terminationTimeoutUnit);
    }
}
