package bdi.glue.jdbc.testdefs;

import bdi.glue.testdefs.User;
import bdi.glue.testdefs.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleDB {

    private static final AtomicInteger idGen = new AtomicInteger();
    public static String generateId() {
        return new SimpleDateFormat("yyyyMMdd'_'HHmmss").format(new Date()) + "_" + idGen.incrementAndGet();
    }

    private final Logger log = LoggerFactory.getLogger(SampleDB.class);

    private final String url;
    private final String username;
    private final String password;

    public static SampleDB createFromWorkingDirAndDefaultCredentials(String workingDir) {
        return createFromWorkingDirAndCredentials(workingDir, "mccallum", "p4ss0rd");
    }

    public static SampleDB createFromWorkingDirAndCredentials(String workingDir, String username, String password) {
        return new SampleDB(workingDir, null, username, password);
    }

    public static SampleDB createFromURLAndCredentials(String url, String username, String password) {
        return new SampleDB(null, url, username, password);
    }

    private SampleDB(String workingDir, String url, String username, String password) {
        this.username = username;
        this.password = password;
        this.url = (url != null) ? (url) : ("jdbc:h2:" + workingDir + "/db_" + generateId());
    }

    public String driver() {
        return "org.h2.Driver";
    }

    public String url() {
        return url;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public void init() {
        log.info("Initializing db: {}", url());
        initDriver();
        try (Connection conn = DriverManager.getConnection(url(), username(), password())) {
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE user (id INT, firstname VARCHAR(50), lastname VARCHAR(50))");

            int nb = 0;
            PreparedStatement pStmt = conn.prepareStatement("INSERT INTO user (id, firstname, lastname) values (?,?,?)");
            for (User user : new UserRepository().all()) {
                pStmt.setInt(1, user.id);
                pStmt.setString(2, user.firstname);
                pStmt.setString(3, user.lastname);
                pStmt.executeUpdate();
                nb++;
            }
            log.info("#{} user(s) inserted", nb);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDriver() {
        try {
            Class.forName(driver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public User get(int id) {
        try (Connection conn = DriverManager.getConnection(url(), username(), password())) {
            PreparedStatement pStmt = conn.prepareStatement("SELECT id, firstname, lastname FROM user where id = ?");
            pStmt.setInt(1, id);
            ResultSet resultSet = pStmt.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
