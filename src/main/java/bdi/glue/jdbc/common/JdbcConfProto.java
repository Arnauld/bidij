package bdi.glue.jdbc.common;

import bdi.glue.env.VariableResolver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JdbcConfProto {
    public String configurationName;
    public String driver;
    public String url;
    public String username;
    public String password;

    public void assertValid() {
        assertThat(configurationName).isNotNull();
        assertThat(driver).isNotNull();
        assertThat(url).isNotNull();
        assertThat(username).isNotNull();
        assertThat(password).isNotNull();
    }

    public JdbcConf jdbcConf() {
        return new JdbcConf(driver, url, username, password);
    }

    public JdbcConf jdbcConf(VariableResolver variableResolver) {
        return new JdbcConf(driver,
                variableResolver.resolve(url),
                variableResolver.resolve(username),
                variableResolver.resolve(password));
    }
}
