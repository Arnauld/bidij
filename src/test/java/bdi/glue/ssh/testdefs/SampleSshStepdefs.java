package bdi.glue.ssh.testdefs;

import bdi.TestSettings;
import bdi.glue.env.VariableResolver;
import bdi.glue.proc.common.ProcStepdefs;
import bdi.glue.proc.common.ProcWorld;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import org.apache.sshd.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleSshStepdefs {

    private final ProcWorld procWorld;
    private final ProcStepdefs proc;
    private final VariableResolver variableResolver;
    //
    private boolean vmUp;
    private SshServer sshd;

    public SampleSshStepdefs(ProcWorld procWorld, ProcStepdefs proc, VariableResolver variableResolver) {
        this.procWorld = procWorld;
        this.proc = proc;
        this.variableResolver = variableResolver;
    }

    @Before
    public void setUp() {
        TestSettings settings = new TestSettings();
        variableResolver.declareVariable("project.basedir", settings.projectDir());
    }

    @After
    public void tearDown() throws Throwable {
        if (vmUp) {
            proc.runCommand("vagrant destroy -f");
            proc.waitLastProcessTermination((int) TimeUnit.MINUTES.toSeconds(2));
        }

        if (sshd != null) {
            sshd.stop();
        }
    }

    @Given("^a default vm popped$")
    public void a_default_vm_popped() throws Throwable {
        proc.defineCurrentDir("/Users/Arnauld/Projects/3rdParties/vagrant-workingdir");
        proc.runCommand("vagrant up");
        vmUp = true;
        proc.waitLastProcessTermination((int) TimeUnit.MINUTES.toSeconds(4));
    }

    @Given("^a sample ssh server running on port (\\d+)$")
    public void a_default_ssh_server_running_on_port(int port) throws Throwable {
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setPasswordAuthenticator((username, password, session) -> password.equals("P4ss0rd"));
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshd.setShellFactory(new ProcessShellFactory(new String[]{"/bin/sh", "-i", "-l"}));
        sshd.start();
    }
}
