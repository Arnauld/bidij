package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum HttpMethod {
    GET,
    PUT,
    POST,
    DELETE,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT,
    PATCH;

    public static HttpMethod lookup(String methodAsString) {
        for (HttpMethod m : HttpMethod.values()) {
            if (m.name().equalsIgnoreCase(methodAsString)) {
                return m;
            }
        }
        return null;
    }
}
