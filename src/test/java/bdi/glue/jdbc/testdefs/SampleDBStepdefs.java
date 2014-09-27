package bdi.glue.jdbc.testdefs;

import bdi.TestSettings;
import bdi.glue.env.VariableResolver;
import bdi.glue.jdbc.common.JdbcConf;
import bdi.glue.jdbc.common.JdbcWorld;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleDBStepdefs {

    private final JdbcWorld jdbcWorld;
    private final VariableResolver variableResolver;

    public SampleDBStepdefs(JdbcWorld jdbcWorld, VariableResolver variableResolver) {
        this.jdbcWorld = jdbcWorld;
        this.variableResolver = variableResolver;
    }

    @Before
    public void configureVariables() {
        variableResolver.declareVariable("idgen", SampleDB::generateId);
        variableResolver.declareVariables(new TestSettings().properties());
    }

    @Given("^a sample database running and defining \"([^\"]*)\" jdbc configuration$")
    public void sampleDBAndStoreAndKeepPArametersUsing(String confName) throws Throwable {
        SampleDB sampleDB = SampleDB.createFromWorkingDirAndDefaultCredentials(new TestSettings().buildDir());
        sampleDB.init();
        jdbcWorld.defineConfAndSetAsCurrent(
                confName,
                new JdbcConf(sampleDB.driver(),
                        sampleDB.url(),
                        sampleDB.username(),
                        sampleDB.password())
        );
    }

    @Given("^a sample database running using configuration \"([^\"]*)\"$")
    public void sampleDBUsingConf(String confName) throws Throwable {
        JdbcConf conf = jdbcWorld.getConf(confName);
        assertThat(conf)
                .describedAs("No conf defined for name '" + confName + "'")
                .isNotNull();
        SampleDB sampleDB = SampleDB.createFromURLAndCredentials(
                        conf.getUrl(),
                        conf.getUsername(),
                        conf.getPassword());
        sampleDB.init();
    }
}
