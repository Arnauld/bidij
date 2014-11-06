package bdi.glue.ssh.common;

import java.util.Stack;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshWorld {

    private SshSessionBuilder sessionBuilder;
    private SshGateway sshGateway = new SshGateway();
    private final Stack<SshSession> sessionStack = new Stack<>();

    public SshSessionBuilder currentSessionBuilder() {
        if (sessionBuilder == null) {
            sessionBuilder = new SshSessionBuilder();
        }
        return sessionBuilder;
    }

    public void defineSshGateway(SshGateway sshGateway) {
        this.sshGateway = sshGateway;
    }

    public SshGateway getSshGateway() {
        return sshGateway;
    }

    public void pushSession(SshSession session) {
        sessionStack.push(session);
    }

    public boolean hasSession() {
        return !sessionStack.isEmpty();
    }

    public SshSession peekSession() {
        if (hasSession()) {
            return sessionStack.peek();
        }
        else {
            throw new SshException("No session registered");
        }
    }
}
