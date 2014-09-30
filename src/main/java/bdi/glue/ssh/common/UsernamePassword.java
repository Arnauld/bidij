package bdi.glue.ssh.common;

import bdi.glue.env.VariableResolver;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UsernamePassword {
    private final String username;
    private final String password;

    public UsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UsernamePassword resolve(VariableResolver variableResolver) {
        return new UsernamePassword(
                resolve(variableResolver, username),
                resolve(variableResolver, password));
    }

    private static String resolve(VariableResolver variableResolver, String s) {
        return s == null ? null : variableResolver.resolve(s);
    }
}
