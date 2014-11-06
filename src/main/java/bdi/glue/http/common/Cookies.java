package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Cookies {
    private Cookies() {
    }

    public static String value(Cookie cookie) {
        return cookie.value();
    }
}
