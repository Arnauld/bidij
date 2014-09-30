package bdi.glue.proc.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.ProcessBuilder.Redirect.appendTo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Proc {
    private static final AtomicInteger idGen = new AtomicInteger();


    private static String newPid() {
        return "pid_" + new SimpleDateFormat("yyyyMMdd'_'HHmmss").format(new Date()) + "_" + idGen.incrementAndGet();
    }

    private final String pid;
    private final String[] cmdarray;
    private final File workingDir;
    private final File outputDir;
    private Process process;

    public Proc(String cmd, File workingDir, File outputDir) {
        this(newPid(), cmd, workingDir, outputDir);
    }

    public Proc(String pid, String cmd, File workingDir, File outputDir) {
        this.pid = pid;
        this.cmdarray = split(cmd);
        this.workingDir = workingDir;
        this.outputDir = outputDir;
    }

    public void start() throws IOException {
        process = new ProcessBuilder(cmdarray)
                .directory(workingDir)
                .redirectOutput(appendTo(getOut()))
                .redirectError(appendTo(getErr()))
                .start();
    }

    private File getErr() {
        return new File(outputDir, pid + ".err");
    }

    public File getOut() {
        return new File(outputDir, pid + ".out");
    }

    private static String[] split(String cmd) {
        StringTokenizer st = new StringTokenizer(cmd);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
            cmdarray[i] = st.nextToken();
        return cmdarray;
    }

    public Process process() {
        return process;
    }

    public void waitForTermination(long timeout, TimeUnit timeoutUnit) throws InterruptedException, TimeoutException {
        if (!process.waitFor(timeout, timeoutUnit))
            throw new TimeoutException("Proc didn't terminate in time");
    }
}
