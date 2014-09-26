package bdi.glue.jdbc.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JdbcConf {

    private final String driver;
    private final String url;
    private final String username;
    private final String password;

    public JdbcConf(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Connection openConnection() throws SQLException {
        initDriver();
        return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
    }

    private void initDriver() {
        try {
            Class.forName(getDriver());
        } catch (ClassNotFoundException e) {
            throw new JdbcException("Fail to load driver '" + driver + "'", e);
        }
    }
}
