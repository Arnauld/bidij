package bdi.glue.ssh.common;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshGateway {

    private Logger log = LoggerFactory.getLogger(SshGateway.class);

    public SshSession openSession(SshSessionBuilder builder) {
        JSch jsch = new JSch();
        SshIdentity identity = builder.getSshIdentity();
        if (identity != null) {
            log.debug("Open session using identity: {}", identity);
            try {
                jsch.addIdentity(identity.getPrivateKeyPath(), identity.getPassphrase());
            } catch (JSchException e) {
                throw new SshException("Fail to add identity " + identity, e);
            }
        }

        try {
            log.debug("Open session on host {} using port {} with username {}",
                    builder.getHost(),
                    builder.getPort(),
                    builder.getUsername());

            Session session = jsch.getSession(
                    builder.getUsername(),
                    builder.getHost(),
                    builder.getPort());
            session.setPassword(builder.getPassword());

            if (!builder.isStrictHostKeyChecking())
                session.setConfig("StrictHostKeyChecking", "no");
            session.connect(builder.getConnectTimeout());   // making a connection with timeout.

            Channel channel = session.openChannel(builder.getChannelType());
            return new SshSession(session, channel);
        } catch (JSchException e) {
            throw new SshException(e);
        }
    }
}
