package bdi.glue.http.common;

import org.json.JSONObject;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface HttpResponse extends Adaptable {
    int statusCode();

    List<Cookie> getCookies(String cookieName);

    String bodyAsText();

    JSONObject bodyAsJson();

    void dispose();
}
