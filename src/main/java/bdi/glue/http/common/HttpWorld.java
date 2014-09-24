package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpWorld {

    private HttpGateway httpGateway;
    private HttpRequestBuilder requestBuilder;
    private HttpResponse response;

    public HttpWorld() {
    }

    public void defineHttpGateway(HttpGateway httpGateway) {
        this.httpGateway = httpGateway;
    }

    public HttpGateway getHttpGateway() {
        return httpGateway;
    }

    public HttpRequestBuilder currentRequestBuilder() {
        if (requestBuilder == null)
            requestBuilder = new HttpRequestBuilder();
        return requestBuilder;
    }

    public void lastResponse(HttpResponse response) {
        this.response = response;
    }

    public HttpResponse lastResponse() {
        return response;
    }
}
