package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpException extends RuntimeException {
    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }
}
