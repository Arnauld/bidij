package bdi.glue.proc.common;

import bdi.glue.env.VariableResolver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.StringAssert;
import org.assertj.core.util.Strings;

import java.io.File;
import java.io.FileInputStream;
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
    public void defineCurrentDir(String dir) throws Throwable {
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
     */
    @When("^I run `([^`]*)`$")
    public void runCommand(String command) throws Throwable {
        Proc proc = new Proc(
                command,
                new File(procWorld.getCurrentDir()),
                new File(procWorld.getOutputDir())
        );
        proc.start();
        procWorld.pushProcess(proc);
    }

    //-------------------------------------------------------------------------
    //   _______ _    _ ______ _   _
    //  |__   __| |  | |  ____| \ | |
    //     | |  | |__| | |__  |  \| |
    //     | |  |  __  |  __| | . ` |
    //     | |  | |  | | |____| |\  |
    //     |_|  |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @Then("^(once finished, )?the last command output should (contain|be|equal to|satisfy):$")
    public void assertLastCommandOutputVerifies(String onceFinished,
                                                String comparator,
                                                String expectedText) throws Throwable {
        Proc proc = procWorld.peekProcess();

        if (!Strings.isNullOrEmpty(onceFinished)) {
            procWorld.waitTermination(proc);
        }

        String s = IOUtils.toString(new FileInputStream(proc.getOut()));
        StringAssert stringAssert = assertThat(s);
        switch (comparator) {
            case "contain":
                stringAssert.contains(expectedText);
                break;
            case "be":
            case "equal to":
                stringAssert.isEqualTo(expectedText);
                break;
            case "satisfy":
                Pattern p = Pattern.compile(expectedText);
                assertThat(p.matcher(s).find()).isTrue();
        }

    }
}
