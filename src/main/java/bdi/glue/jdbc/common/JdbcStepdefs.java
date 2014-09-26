package bdi.glue.jdbc.common;

import bdi.glue.env.VariableResolver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.IntegerAssert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JdbcStepdefs {

    private final JdbcWorld jdbcWorld;
    private final VariableResolver variableResolver;

    public JdbcStepdefs(JdbcWorld jdbcWorld, VariableResolver variableResolver) {
        this.jdbcWorld = jdbcWorld;
        this.variableResolver = variableResolver;
    }

    //-------------------------------------------------------------------------
    //   / ____|_   _\ \    / /  ____| \ | |
    //  | |  __  | |  \ \  / /| |__  |  \| |
    //  | | |_ | | |   \ \/ / |  __| | . ` |
    //  | |__| |_| |_   \  /  | |____| |\  |
    //   \_____|_____|   \/   |______|_| \_|
    //-------------------------------------------------------------------------

    @Given("^the \"([^\"]*)\" jdbc configuration has been applied$")
    public void defineConfAsCurrent(String confName) throws Throwable {
        jdbcWorld.defineCurrentConf(confName);
    }

    @Given("^the following jdbc configurations:$")
    public void defineConfs(List<JdbcConfProto> protoz) throws Throwable {
        for (JdbcConfProto proto : protoz) {
            jdbcWorld.defineConf(proto.configurationName, proto.jdbcConf(variableResolver));
        }
    }

    //-------------------------------------------------------------------------
    //    \ \        / / |  | |  ____| \ | |
    //     \ \  /\  / /| |__| | |__  |  \| |
    //      \ \/  \/ / |  __  |  __| | . ` |
    //       \  /\  /  | |  | | |____| |\  |
    //        \/  \/   |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @When("^a query is made on table \"([^\"]*)\"$")
    public void selectRowData(String tableName) throws Throwable {
        try (Connection c = jdbcWorld.currentConf().openConnection()) {
            int maxFetchSize = jdbcWorld.maxFetchSize();

            Statement stmt = c.createStatement();
            stmt.setFetchSize(maxFetchSize);

            Rows rows = new Rows();
            ResultSet rSet = stmt.executeQuery("select count(*) from " + tableName);
            if (rSet.next())
                rows.defineNumberOfRows(rSet.getInt(1));
            rSet.close();

            rSet = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = rSet.getMetaData();
            int nbCols = rsmd.getColumnCount();

            rows.defineColumns(rsmd);

            while (rSet.next()) {
                Object[] o = new Object[nbCols];
                for (int i = 0; i < nbCols; i++) {
                    o[i] = rSet.getObject(i + 1);
                }
                rows.appendRow(o);
                if (--maxFetchSize == 0)
                    return;
            }

            jdbcWorld.lastResult(rows);
        }
    }

    //-------------------------------------------------------------------------
    //   _______ _    _ ______ _   _
    //  |__   __| |  | |  ____| \ | |
    //     | |  | |__| | |__  |  \| |
    //     | |  |  __  |  __| | . ` |
    //     | |  | |  | | |____| |\  |
    //     |_|  |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @Then("^the number of rows returned should be (greater than|greater than or equal to|equal to|lesser than|lesser than or equal to) (\\d+)$")
    public void assertNumberOfRowsOnLastResult(String comparator, int expectedNbRows) throws Throwable {
        Rows rows = jdbcWorld.lastResult();
        int nbRows = rows.getNbRows();
        IntegerAssert integerAssert = assertThat(nbRows);

        switch (comparator) {
            case "equal to":
                integerAssert.isEqualTo(expectedNbRows);
                break;
            case "greater than":
                integerAssert.isGreaterThan(expectedNbRows);
                break;
            case "greater than or equal to":
                integerAssert.isGreaterThanOrEqualTo(expectedNbRows);
                break;
            case "lesser than":
                integerAssert.isLessThan(expectedNbRows);
                break;
            case "lesser than or equal to":
                integerAssert.isLessThanOrEqualTo(expectedNbRows);
                break;
        }
    }
}
