package bdi.glue.ssh.testdefs;

import bdi.glue.proc.common.ProcStepdefs;
import bdi.glue.proc.common.ProcWorld;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleSshStepdefs {

    private final ProcWorld procWorld;
    private final ProcStepdefs proc;
    private boolean vmUp;

    public SampleSshStepdefs(ProcWorld procWorld, ProcStepdefs proc) {
        this.procWorld = procWorld;
        this.proc = proc;
    }

    @After
    public void tearDown() throws Throwable {
        if (vmUp) {
            proc.runCommand("vagrant destroy -f");
            proc.waitLastProcessTermination((int) TimeUnit.MINUTES.toSeconds(2));
        }
    }

    @Given("^a default vm popped$")
    public void a_default_vm_popped() throws Throwable {
        proc.defineCurrentDir("/Users/Arnauld/Projects/3rdParties/vagrant-workingdir");
        proc.runCommand("vagrant up");
        vmUp = true;
        proc.waitLastProcessTermination((int) TimeUnit.MINUTES.toSeconds(4));
    }
}
