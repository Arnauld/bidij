package bdi.glue.ssh.common;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshSession {
    private final Session session;
    private final Channel channel;

    public SshSession(Session session, Channel channel) {
        this.session = session;
        this.channel = channel;
    }
}
