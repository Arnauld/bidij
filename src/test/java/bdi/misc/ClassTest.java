package bdi.misc;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ClassTest {

    @Test
    public void isAssignable() {
        assertThat(HttpResponse.class.isAssignableFrom(CloseableHttpResponse.class)).isTrue();
        assertThat(CloseableHttpResponse.class.isAssignableFrom(HttpResponse.class)).isFalse();
    }
}
