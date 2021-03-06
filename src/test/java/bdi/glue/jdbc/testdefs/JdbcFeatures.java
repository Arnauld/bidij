package bdi.glue.jdbc.testdefs;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"bdi.glue.jdbc.testdefs", "bdi.glue.jdbc.common"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/jdbc")
public class JdbcFeatures {
}
