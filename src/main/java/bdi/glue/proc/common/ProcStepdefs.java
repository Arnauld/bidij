package bdi.glue.proc.common;

import bdi.glue.env.VariableResolver;
import bdi.glue.util.StringAssertions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.StringAssert;
import org.assertj.core.util.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ProcStepdefs {

    private final ProcWorld procWorld;
    private final VariableResolver variableResolver;

    public ProcStepdefs(ProcWorld procWorld, VariableResolver variableResolver) {
        this.procWorld = procWorld;
        this.variableResolver = variableResolver;
    }

    //-------------------------------------------------------------------------
    //   / ____|_   _\ \    / /  ____| \ | |
    //  | |  __  | |  \ \  / /| |__  |  \| |
    //  | | |_ | | |   \ \/ / |  __| | . ` |
    //  | |__| |_| |_   \  /  | |____| |\  |
    //   \_____|_____|   \/   |______|_| \_|
    //-------------------------------------------------------------------------

    @Given("^the current working directory has been set to \"(.*?)\"$")
    public void defineCurrentDir(String dir) {
        procWorld.setCurrentDir(variableResolver.resolve(dir));
    }

    //-------------------------------------------------------------------------
    //    \ \        / / |  | |  ____| \ | |
    //     \ \  /\  / /| |__| | |__  |  \| |
    //      \ \/  \/ / |  __  |  __| | . ` |
    //       \  /\  /  | |  | | |____| |\  |
    //        \/  \/   |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    /**
     * Executes the specified command and arguments in a separate process with the specified environment.
     *
     * @param command to execute
     * @throws IOException when spawning process
     */
    @When("^I run `([^`]*)`$")
    public void runCommand(String command) throws IOException {
        Proc proc = new Proc(
                command,
                new File(procWorld.getCurrentDir()),
                new File(procWorld.getOutputDir())
        );
        proc.start();
        procWorld.pushProcess(proc);
    }

    @When("^I wait for the process to terminate$")
    public void waitLastProcessTermination() throws TimeoutException, InterruptedException {
        procWorld.waitLastProcessTermination();
    }


    @When("^I wait at most (\\d+) seconds for the process to terminate$")
    public void waitLastProcessTermination(int timeoutInSeconds) throws TimeoutException, InterruptedException {
        procWorld.waitLastProcessTermination(timeoutInSeconds, TimeUnit.SECONDS);
    }

    //-------------------------------------------------------------------------
    //   _______ _    _ ______ _   _
    //  |__   __| |  | |  ____| \ | |
    //     | |  | |__| | |__  |  \| |
    //     | |  |  __  |  __| | . ` |
    //     | |  | |  | | |____| |\  |
    //     |_|  |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @Then("^(once finished, )?the last command output should (contain|be|equal to|satisfy|partially satisfy):$")
    public void assertLastCommandOutputVerifies(String onceFinished,
                                                String comparator,
                                                String expectedText) throws TimeoutException, InterruptedException, IOException {
        Proc proc = procWorld.peekProcess();

        if (!Strings.isNullOrEmpty(onceFinished)) {
            procWorld.waitTermination(proc);
        }

        String s = IOUtils.toString(new FileInputStream(proc.getOut()));
        StringAssertions.apply(comparator, s, expectedText);
    }

}
