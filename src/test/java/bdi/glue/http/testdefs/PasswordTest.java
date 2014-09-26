package bdi.glue.http.testdefs;

import org.eclipse.jetty.util.security.Password;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PasswordTest {

    @Test
    public void usecase() {
        assertThat(Password.obfuscate("serverp")).isEqualTo("OBF:1y0y1vn61vnw1zsv1vn61vnw1y0s");
        assertThat(Password.obfuscate("clientp")).isEqualTo("OBF:1uuq1xfb1vne1t331vno1xfr1uvg");
    }
}
