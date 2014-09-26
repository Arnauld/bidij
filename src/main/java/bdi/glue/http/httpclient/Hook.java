package bdi.glue.http.httpclient;

import bdi.glue.http.common.HttpResponse;
import bdi.glue.http.common.HttpWorld;
import cucumber.api.java.After;
import cucumber.api.java.Before;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Hook {

    private final HttpClientWorld httpClientWorld;
    private final HttpWorld bdiWorld;

    public Hook(HttpClientWorld httpClientWorld, HttpWorld bdiWorld) {
        this.httpClientWorld = httpClientWorld;
        this.bdiWorld = bdiWorld;
    }

    @Before(order = 1000)
    public void initHttpGateway() {
        HttpClientGateway httpGateway = new HttpClientGateway(httpClientWorld);
        bdiWorld.defineHttpGateway(httpGateway);
    }

    @After(order = 1000)
    public void tearDown() {
        HttpResponse response = bdiWorld.lastResponse();
        if (response != null)
            response.dispose();
    }
}
