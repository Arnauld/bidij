package bdi.glue.ssh.common;

import bdi.glue.env.VariableResolver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.StringAssert;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

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
        SshSession sshSession = sshGateway.openSession(sshWorld.currentSessionBuilder());
        sshWorld.pushSession(sshSession);
    }

    //-------------------------------------------------------------------------
    //    \ \        / / |  | |  ____| \ | |
    //     \ \  /\  / /| |__| | |__  |  \| |
    //      \ \/  \/ / |  __  |  __| | . ` |
    //       \  /\  /  | |  | | |____| |\  |
    //        \/  \/   |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @When("^through ssh, I run `([^`]+)`$")
    public void runSshCommand(String command) throws Throwable {
        SshSession sshSession = sshWorld.peekSession();
        sshSession.runCommand(command);
    }

    //-------------------------------------------------------------------------
    //   _______ _    _ ______ _   _
    //  |__   __| |  | |  ____| \ | |
    //     | |  | |__| | |__  |  \| |
    //     | |  |  __  |  __| | . ` |
    //     | |  | |  | | |____| |\  |
    //     |_|  |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @Then("^within (\\d+) (seconds?|minutes?), the ssh session output should (contain|satisfy) \"(.*?)\"$")
    public void assertSshOutputVerifies(long timeout,
                                        String timeoutUnit,
                                        String comparator,
                                        String expectedText) throws Throwable {
        TimeUnit unit = TimeUnit.SECONDS;
        if (timeoutUnit.toLowerCase().startsWith("minute"))
            unit = TimeUnit.MINUTES;

        await().atMost(timeout, unit).until(() -> {
                    SshSession sshSession = sshWorld.peekSession();
                    String out = sshSession.getOut();
                    StringAssert stringAssert = assertThat(out);
                    switch (comparator) {
                        case "contain":
                            stringAssert.contains(expectedText);
                            break;
                        case "satisfy":
                            Pattern p = Pattern.compile(expectedText);
                            assertThat(p.matcher(out).find()).isTrue();
                    }
                }
        );
    }


}
