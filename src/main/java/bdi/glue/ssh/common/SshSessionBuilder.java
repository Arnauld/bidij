package bdi.glue.ssh.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshSessionBuilder {
    private String host;
    private int port = 22;
    private UsernamePassword usernamePassword;
    private int connectTimeout = 30000;
    private String channelType = "shell";
    private boolean strictHostKeyChecking;
    private SshIdentity sshIdentity;


    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public SshSessionBuilder host(String hostMaybeWithPort) {
        int index = hostMaybeWithPort.indexOf(":");
        if (index > -1) {
            String portAsStr = hostMaybeWithPort.substring(index + 1);
            try {
                port = Integer.parseInt(portAsStr);
            } catch (NumberFormatException nfe) {
                throw new SshException("Invalid port '" + portAsStr + "'");
            }
            host = hostMaybeWithPort.substring(0, index);
        } else {
            host = hostMaybeWithPort;
        }
        return this;
    }

    public SshSessionBuilder usernamePassword(UsernamePassword usernamePassword) {
        this.usernamePassword = usernamePassword;
        return this;
    }

    public String getUsername() {
        return usernamePassword.getUsername();
    }

    public String getPassword() {
        return usernamePassword.getPassword();
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public SshSessionBuilder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public String getChannelType() {
        return channelType;
    }

    public SshSessionBuilder channelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public boolean isStrictHostKeyChecking() {
        return strictHostKeyChecking;
    }

    public SshSessionBuilder strictHostKeyChecking(boolean strictHostKeyChecking) {
        this.strictHostKeyChecking = strictHostKeyChecking;
        return this;
    }

    public SshIdentity getSshIdentity() {
        return sshIdentity;
    }

    public SshSessionBuilder declareIdentity(String privateKeyPass, String passphrase) {
        sshIdentity = new SshIdentity(privateKeyPass, passphrase);
        return this;
    }
}
