package bdi.glue.env;


import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableResolverTest {
    private VariableResolver templateEngine;
    private Map<String, String> variables;

    @Before
    public void setUp() {
        templateEngine = new VariableResolver();
        variables = newHashMap("who", "World", "when", "tomorrow");
    }

    private <K, V> Map<K, V> newHashMap(K k1, V v1, K k2, V v2) {
        HashMap<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Test
    public void resolve_basic() {
        String resolved = templateEngine.resolve("${who}${when}", VariableResolver.convert(variables));
        assertThat(resolved).isEqualTo("Worldtomorrow");
    }

    @Test
    public void resolve() {
        String resolved = templateEngine.resolve("Hello ${who}!!", VariableResolver.convert(variables));
        assertThat(resolved).isEqualTo("Hello World!!");
    }

    @Test
    public void resolve_unknownVariableIsLeftAsIs() {
        String resolved = templateEngine.resolve("Hello ${whom} ${when}!!", VariableResolver.convert(variables));
        assertThat(resolved).isEqualTo("Hello ${whom} tomorrow!!");
    }
}