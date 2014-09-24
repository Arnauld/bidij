package bdi.glue.http.testdefs;

import bdi.glue.GlobalWorld;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import org.eclipse.jetty.server.Server;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleAppStepdefs {

    private final GlobalWorld globalWorld;
    private Server server;

    public SampleAppStepdefs(GlobalWorld globalWorld) {
        this.globalWorld = globalWorld;
    }

    @After
    public void tearDown() throws Exception {
        if(server!=null) {
            server.stop();
            server.join();
        }
    }

    @Given("^a sample server running on port (\\d+)$")
    public void startServer(int port) throws Exception {
        server = SampleApp.start(port);
        globalWorld.put(Server.class, server);
    }
}
