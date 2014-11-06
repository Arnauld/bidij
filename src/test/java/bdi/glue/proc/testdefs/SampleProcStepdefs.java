package bdi.glue.proc.testdefs;

import bdi.TestSettings;
import bdi.glue.env.VariableResolver;
import bdi.glue.proc.common.ProcWorld;
import cucumber.api.java.en.Given;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleProcStepdefs {

    private static final AtomicInteger idGen = new AtomicInteger();

    private final ProcWorld procWorld;
    private final VariableResolver variableResolver;

    public SampleProcStepdefs(ProcWorld procWorld, VariableResolver variableResolver) {
        this.procWorld = procWorld;
        this.variableResolver = variableResolver;
    }

    @Given("^a new temporary directory kept in variable \"(.*?)\"$")
    public void a_new_temporary_directory_kept_in_variable(String variableName) {
        String buildDir = new TestSettings().buildDir();
        String id = "proc" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + idGen.incrementAndGet();
        File tmpDir = new File(buildDir, id);
        assertThat(tmpDir.mkdirs()).isTrue();

        variableResolver.declareVariable(variableName, tmpDir.getAbsolutePath());
    }
}
