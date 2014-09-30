package bdi.glue.ssh.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshWorld {

    private SshSessionBuilder sessionBuilder;
    private SshGateway sshGateway = new SshGateway();

    public SshSessionBuilder currentSessionBuilder() {
        if (sessionBuilder == null)
            sessionBuilder = new SshSessionBuilder();
        return sessionBuilder;
    }

    public void defineSshGateway(SshGateway sshGateway) {
        this.sshGateway = sshGateway;
    }

    public SshGateway getSshGateway() {
        return sshGateway;
    }
}
