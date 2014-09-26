package bdi.glue.jdbc.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JdbcException extends RuntimeException {
    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcException(Throwable cause) {
        super(cause);
    }
}
