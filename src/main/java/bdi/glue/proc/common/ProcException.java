package bdi.glue.proc.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ProcException extends RuntimeException {
    public ProcException(String message) {
        super(message);
    }

    public ProcException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcException(Throwable cause) {
        super(cause);
    }
}
