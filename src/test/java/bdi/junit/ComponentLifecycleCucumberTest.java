package bdi.junit;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ComponentLifecycleCucumberTest {

    @Test
    public void before_hook_should_be_invoked() {
        // Given
        ComponentLifecycleCucumber cucumber = new ComponentLifecycleCucumber();
        SimpleHooks hooks = new SimpleHooks();

        // When
        cucumber.before(Arrays.asList(hooks));

        // Then
        assertThat(hooks.invoked).containsExactly("before");
    }

    @Test
    public void before_hook_should_be_invoked_in_order() {
        // Given
        ComponentLifecycleCucumber cucumber = new ComponentLifecycleCucumber();
        HooksWithOrder hooks = new HooksWithOrder();

        // When
        cucumber.before(Arrays.asList(hooks));

        // Then
        assertThat(hooks.invoked).containsExactly("before900", "before1000", "before1100", "before");
    }

    @Test
    public void before_hook_should_be_invoked_in_order_when_matching_tag() {
        // Given
        ComponentLifecycleCucumber cucumber = new ComponentLifecycleCucumber("@ui");
        HooksWithOrderAndTags hooks = new HooksWithOrderAndTags();

        // When
        cucumber.before(Arrays.asList(hooks));

        // Then
        assertThat(hooks.invoked).containsExactly("before900", "before1000", "before");
    }


    @Test
    public void after_hook_should_be_invoked() {
        // Given
        ComponentLifecycleCucumber cucumber = new ComponentLifecycleCucumber();
        SimpleHooks hooks = new SimpleHooks();

        // When
        cucumber.after(Arrays.asList(hooks));

        // Then
        assertThat(hooks.invoked).containsExactly("after");
    }

    public static class SimpleHooks {
        public List<String> invoked = new ArrayList<>();

        @Before
        public void before() {
            invoked.add("before");
        }
        @After
        public void after() {
            invoked.add("after");
        }
    }

    public static class HooksWithOrder {
        public List<String> invoked = new ArrayList<>();

        @Before(order = 1000)
        public void before1000() {
            invoked.add("before1000");
        }

        @Before(order = 1100)
        public void before1100() {
            invoked.add("before1100");
        }

        @Before(order = 900)
        public void before900() {
            invoked.add("before900");
        }
        @Before
        public void before() {
            invoked.add("before");
        }

        @After
        public void after() {
            invoked.add("after");
        }
    }

    public static class HooksWithOrderAndTags {
        public List<String> invoked = new ArrayList<>();

        @Before(order = 1000, value = {"@ui"})
        public void before1000() {
            invoked.add("before1000");
        }

        @Before(order = 1100, value = {"@ws"})
        public void before1100() {
            invoked.add("before1100");
        }

        @Before(order = 900, value = {"~@ws"})
        public void before900() {
            invoked.add("before900");
        }
        @Before
        public void before() {
            invoked.add("before");
        }

        @After
        public void after() {
            invoked.add("after");
        }
    }

}