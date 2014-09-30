package bdi.glue.proc.testdefs;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"bdi.glue.proc.testdefs", "bdi.glue.proc.common"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/proc")
public class ProcFeatures {
}
