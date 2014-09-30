package bdi.glue.ssh.common;

import bdi.glue.env.VariableResolver;
import cucumber.api.java.en.Given;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshStepdefs {

    private final SshWorld sshWorld;
    private final VariableResolver variableResolver;

    public SshStepdefs(SshWorld sshWorld, VariableResolver variableResolver) {
        this.sshWorld = sshWorld;
        this.variableResolver = variableResolver;
    }

    //-------------------------------------------------------------------------
    //   / ____|_   _\ \    / /  ____| \ | |
    //  | |  __  | |  \ \  / /| |__  |  \| |
    //  | | |_ | | |   \ \/ / |  __| | . ` |
    //  | |__| |_| |_   \  /  | |____| |\  |
    //   \_____|_____|   \/   |______|_| \_|
    //-------------------------------------------------------------------------


    @Given("^a ssh private key at \"(.*?)\" with no passphrase$")
    public void definePrivateKeyIdentity(String privateKeyPath) throws Throwable {
        String home = System.getProperty("user.home");
        privateKeyPath = variableResolver.resolve(privateKeyPath);
        sshWorld.currentSessionBuilder()
                .declareIdentity(privateKeyPath.replace("~", home), null);
    }

    @Given("^an interactive ssh session opened on \"(.*?)\" with the following credentials:$")
    public void openInteractiveSession(String host, List<UsernamePassword> credentials) throws Throwable {
        UsernamePassword usernamePassword = credentials.get(0).resolve(variableResolver);
        sshWorld.currentSessionBuilder()
                .host(variableResolver.resolve(host))
                .usernamePassword(usernamePassword);
        openSession();
    }

    private void openSession() {
        SshGateway sshGateway = sshWorld.getSshGateway();
        sshGateway.openSession(sshWorld.currentSessionBuilder());
    }
}
