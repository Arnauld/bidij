package bdi.glue.http.testdefs;

import bdi.glue.http.common.RawHttpStepdefs;
import bdi.glue.http.httpclient.HttpClientHooks;
import bdi.junit.ComponentLifecycleCucumber;
import bdi.junit.PicoContainerRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpJUnitTest {

    @Rule
    public final PicoContainerRule container = new PicoContainerRule(
            // app
            SampleAppStepdefs.class,
            SampleAppHooks.class,
            // lib
            HttpClientHooks.class,
            RawHttpStepdefs.class).using(new ComponentLifecycleCucumber());

    @Test
    public void get_on_https() throws Exception {
        /*
            @http_secure
            @http_secure__allow_all_hostname
            Scenario: GET on https

            Given a sample server running on port 8080 and on secure port 8083
            And an host set to "https://localhost:8083"
            When a GET request is made to "/users"
            Then the response status code should be 200
        */

        container.using(new ComponentLifecycleCucumber("@http_secure", "@http_secure__allow_all_hostname"));

        SampleAppStepdefs appSteps = container.get(SampleAppStepdefs.class);
        RawHttpStepdefs httpSteps = container.get(RawHttpStepdefs.class);

        appSteps.startServer(8080, 8083);
        httpSteps.given_a_host_set_to("https://localhost:8083");
        httpSteps.when_a_GET_request_is_made_to("/users");
        httpSteps.then_response_status_code_should_be(200);
    }
}
