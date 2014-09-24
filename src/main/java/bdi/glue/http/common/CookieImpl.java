package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CookieImpl implements Cookie {
    private final String cookieName;
    private final String cookieValue;

    public CookieImpl(String cookieName, String cookieValue) {
        this.cookieName = cookieName;
        this.cookieValue = cookieValue;
    }

    @Override
    public String name() {
        return cookieName;
    }

    @Override
    public String value() {
        return cookieValue;
    }
}
