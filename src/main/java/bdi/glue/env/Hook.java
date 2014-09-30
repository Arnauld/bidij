package bdi.glue.env;

import cucumber.api.java.Before;
import org.junit.internal.AssumptionViolatedException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Hook {

    @Before("@Ignore")
    public void ignore() {
        throw new AssumptionViolatedException("Ignored");
    }
}
