package bdi.glue.http.testdefs;

import bdi.TestSettings;
import bdi.glue.GlobalWorld;
import bdi.glue.http.common.HttpWorld;
import bdi.glue.http.testdefs.app.SampleApp;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import org.eclipse.jetty.server.Server;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleAppStepdefs {

    private final GlobalWorld globalWorld;
    private final HttpWorld httpWorld;
    private Server server;
    private TestSettings testSettings;

    public SampleAppStepdefs(GlobalWorld globalWorld, HttpWorld httpWorld) {
        this.globalWorld = globalWorld;
        this.httpWorld = httpWorld;
    }

    @Before
    public void setUp() {
        testSettings = new TestSettings();
    }

    @After
    public void tearDown() throws Exception {
        if (server != null) {
            server.stop();
            server.join();
        }
    }

    @Given("^a sample server running on port (\\d+)$")
    public void startServer(int port) throws Exception {
        server = new SampleApp().start(port);
        globalWorld.put(Server.class, server);
    }

    @Given("^a sample server running on port (\\d+) and on secure port (\\d+)$")
    public void startServer(int port, int securedPort) throws Exception {
        String projectDir = testSettings.projectDir();

        server = new SampleApp().start(port, securedPort, projectDir + "/data/keystore");
        globalWorld.put(Server.class, server);
    }

    @Given("^default basic auth credentials set$")
    public void default_basic_auth_credentials_set() throws Throwable {
        httpWorld.currentRequestBuilder()
                .basicAuthCredentials(
                        testSettings.getProperty("username"),
                        testSettings.getProperty("password"));
    }
}
