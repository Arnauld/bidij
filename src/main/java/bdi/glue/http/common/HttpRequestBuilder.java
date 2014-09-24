package bdi.glue.http.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpRequestBuilder {
    private String methodAsString;
    private String url;
    private List<Header> headers = new ArrayList<>();
    private List<Cookie> cookies = new ArrayList<>();
    private String body;
    private String contentType;

    public HttpRequestBuilder method(String methodAsString) {
        this.methodAsString = methodAsString;
        return this;
    }

    public HttpRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public HttpRequestBuilder cookie(String cookieName, String cookieValue) {
        this.cookies.add(new CookieImpl(cookieName, cookieValue));
        return this;
    }

    public HttpRequestBuilder header(Header header) {
        this.headers.add(header);
        return this;
    }

    public HttpRequestBuilder headers(Header... headers) {
        for (Header header : headers) {
            header(header);
        }
        return this;
    }


    public HttpRequestBuilder headers(Iterable<Header> headers) {
        for (Header header : headers) {
            header(header);
        }
        return this;
    }

    public String getMethodAsString() {
        return methodAsString;
    }

    public String generateUrl() {
        if (url.startsWith("http://"))
            return url;
        else if(url.startsWith("/")) {
            String host = headers
                    .stream()
                    .filter((Header h) -> h.name.equalsIgnoreCase("host"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Neither a complete URL nor a Host header has been defined (url: " + url + ")"))
                    .value;
            return "http://" + host + url;
        }
        else
            return "http://" + url;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public HttpRequestBuilder body(String body) {
        this.body = body;
        return this;
    }

    public HttpRequestBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
