package bdi.glue.ssh.testdefs;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"bdi.glue.ssh.testdefs", "bdi.glue.ssh.common", "bdi.glue.env"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/ssh")
public class SshFeatures {
}
