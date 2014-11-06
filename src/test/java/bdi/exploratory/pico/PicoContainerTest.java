package bdi.exploratory.pico;

import bdi.glue.http.common.HttpWorld;
import bdi.glue.http.common.RawHttpStepdefs;
import cucumber.runtime.java.picocontainer.PicoFactory;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PicoContainerTest {

    @Test
    public void usecase_direct_picocontainer() throws Throwable {
        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.addComponent(RawHttpStepdefs.class);
        pico.start();

        try {
            pico.getComponent(RawHttpStepdefs.class);
            fail("Actually all dependencies must be declared within pico");
        } catch (Exception e) {
            assertThat(e).hasMessageContaining("unsatisfied dependency 'class bdi.glue.http.common.HttpWorld'");
        }
    }

    @Test
    public void usecase_through_picofactory() throws Throwable {
        PicoFactory pico = new PicoFactory();
        pico.addClass(RawHttpStepdefs.class);
        pico.start();

        RawHttpStepdefs stepdefs = pico.getInstance(RawHttpStepdefs.class);
        assertThat(stepdefs).isNotNull();

        HttpWorld world = pico.getInstance(HttpWorld.class);
        assertThat(world).isNotNull();
    }
}
