package bdi.glue.http.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpRequestBuilder {

    private String methodAsString;
    private String requestPath;
    private List<Header> headers = new ArrayList<>();
    private List<Cookie> cookies = new ArrayList<>();
    private String body;
    private String contentType;
    private String username;
    private String password;
    private List<Parameter> parameters = new ArrayList<>();
    private List<String> cookiesToRemove = new ArrayList<>();

    public HttpRequestBuilder method(String methodAsString) {
        this.methodAsString = methodAsString;
        return this;
    }

    public HttpRequestBuilder cookie(String cookieName, String cookieValue) {
        this.cookies.add(new CookieImpl(cookieName, cookieValue));
        return this;
    }

    public List<Cookie> getCookies() {
        return cookies;
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

    public HttpRequestBuilder requestPath(String requestPath) {
        this.requestPath = requestPath;
        return this;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public HttpRequestBuilder body(String body) {
        this.body = body;
        return this;
    }

    public String getBody() {
        return body;
    }

    public HttpRequestBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public HttpRequestBuilder basicAuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public HttpRequestBuilder parameters(List<Parameter> parameters) {
        this.parameters.addAll(parameters);
        return this;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void cookieToRemove(String cookieName) {
        this.cookiesToRemove.add(cookieName);
    }

    public List<String> getCookiesToRemove() {
        return cookiesToRemove;
    }
}
