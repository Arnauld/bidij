package bdi.glue.ssh.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SshIdentity {
    private final String privateKeyPath;
    private final String passphrase;

    public SshIdentity(String privateKeyPath, String passphrase) {
        this.privateKeyPath = privateKeyPath;
        this.passphrase = passphrase;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public String getPassphrase() {
        return passphrase;
    }

    @Override
    public String toString() {
        return "SshIdentity{" +
                "privateKeyPath='" + privateKeyPath + '\'' +
                ", passphrase='" + passphrase + '\'' +
                '}';
    }
}
