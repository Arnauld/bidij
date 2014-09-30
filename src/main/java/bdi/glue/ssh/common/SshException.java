package bdi.glue.ssh.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshException extends RuntimeException {
    public SshException(String message) {
        super(message);
    }

    public SshException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshException(Throwable cause) {
        super(cause);
    }
}
