package bdi.glue.env;


import bdi.glue.util.New;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableResolverTest {
    private VariableResolver templateEngine;
    private Map<String, String> variables;

    @Before
    public void setUp() {
        templateEngine = new VariableResolver();
        variables = New.hashMap("who", "World", "when", "tomorrow");
    }

    @Test
    public void resolve_basic() {
        String resolved = templateEngine.resolve("${who}${when}", variables);
        assertThat(resolved).isEqualTo("Worldtomorrow");
    }

    @Test
    public void resolve() {
        String resolved = templateEngine.resolve("Hello ${who}!!", variables);
        assertThat(resolved).isEqualTo("Hello World!!");
    }

    @Test
    public void resolve_unknownVariableIsLeftAsIs() {
        String resolved = templateEngine.resolve("Hello ${whom} ${when}!!", variables);
        assertThat(resolved).isEqualTo("Hello ${whom} tomorrow!!");
    }
}