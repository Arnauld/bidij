package bdi.glue.ssh.common;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshSession {
    private Logger log = LoggerFactory.getLogger(SshSession.class);

    private final Session session;
    private final Channel channel;
    private Charset charset = Charset.forName("UTF8");
    private final ByteArrayOutputStream bout = new ByteArrayOutputStream();

    public SshSession(Session session, String channelType) throws JSchException {
        this.session = session;
        this.channel = openChannel(session, channelType);
    }

    private Channel openChannel(Session session, String channelType) throws JSchException {
        Channel channel = session.openChannel(channelType);
        channel.setOutputStream(new TeeOutputStream(bout, System.out));
        channel.connect();
        return channel;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void runCommand(String command) throws IOException {
        Writer writer = new OutputStreamWriter(channel.getOutputStream(), getCharset());
        writer.write(command);
        writer.write("\n");
        writer.flush();
    }

    public String getOut() {
        try {
            return bout.toString("utf8");
        } catch (UnsupportedEncodingException e) {
            throw new SshException(e);
        }
    }

    public Session getSession() {
        return session;
    }
}