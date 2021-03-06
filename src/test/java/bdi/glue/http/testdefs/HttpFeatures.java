package bdi.glue.http.testdefs;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"bdi.glue.http.testdefs", "bdi.glue.http.common", "bdi.glue.http.httpclient"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/http")
public class HttpFeatures {
}
