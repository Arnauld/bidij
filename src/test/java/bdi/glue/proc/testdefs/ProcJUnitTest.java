package bdi.glue.proc.testdefs;

import bdi.glue.proc.common.ProcStepdefs;
import bdi.junit.ComponentLifecycleCucumber;
import bdi.junit.PicoContainerRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ProcJUnitTest {

    @Rule
    public final PicoContainerRule container = new PicoContainerRule(
            // app
            SampleProcStepdefs.class,
            // lib
            ProcStepdefs.class).using(new ComponentLifecycleCucumber());

    @Test
    public void simple_procs() throws IOException, TimeoutException, InterruptedException {
        /*
            @linux
            Scenario: Simple Process

            Given a new temporary directory kept in variable "workingDir"
            And the current working directory has been set to "${workingDir}"
            When I run `touch filemode.file`
            And I run `ls -al`
            Then once finished, the last command output should satisfy:
            """
            \Q-rw-r--r--\E (.+) filemode.file
            """
        */

        ProcStepdefs procSteps = container.get(ProcStepdefs.class);
        SampleProcStepdefs appSteps = container.get(SampleProcStepdefs.class);


        appSteps.a_new_temporary_directory_kept_in_variable("workingDir");
        procSteps.defineCurrentDir("${workingDir}");
        procSteps.runCommand("touch filemode.file");
        procSteps.runCommand("ls -al");
        procSteps.assertLastCommandOutputVerifies("once finished, ", "partially match", "\\Q-rw-r--r--\\E (.+) filemode\\.file");
    }
}
