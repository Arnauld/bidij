package samples;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(glue = {"bdi.glue.http.common", "bdi.glue.http.httpclient"})
public class SampleFeatureTest {
}
