package bdi.glue.http.httpclient;

import bdi.glue.http.common.Cookie;
import bdi.glue.http.common.CookieImpl;
import bdi.glue.http.common.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpResponseAdapter implements HttpResponse {
    private final CloseableHttpResponse response;
    private final CookieStore cookieStore;

    public HttpResponseAdapter(CloseableHttpResponse response, CookieStore cookieStore) {
        this.response = response;
        this.cookieStore = cookieStore;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAdapter(Class<T> type) {
        if (type.isInstance(response))
            return (T) response;
        return null;
    }

    @Override
    public int statusCode() {
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public List<Cookie> getCookies(String cookieName) {
        Function<org.apache.http.cookie.Cookie, Cookie> mapper =
                (c) -> new CookieImpl(c.getName(), c.getValue());
        return cookieStore.getCookies()
                .stream()
                .filter((c) -> c.getName().equals(cookieName))
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public String bodyAsText() {
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Fail to read response entity", e);
        }
    }

    @Override
    public JSONObject bodyAsJson() {
        try {
            return new JSONObject(bodyAsText());
        } catch (JSONException e) {
            throw new RuntimeException("Fail to decode JSON", e);
        }
    }

    @Override
    public void dispose() {
        try {
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            //ignored
        }
    }
}
