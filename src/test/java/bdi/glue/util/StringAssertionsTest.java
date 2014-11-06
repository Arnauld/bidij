package bdi.glue.util;

import org.assertj.core.api.Assertions;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class StringAssertionsTest {

    @Parameterized.Parameters(name = "{index} :: {0}|{1}|{2}|{3}")
    public static List<Object[]> input() {
        return Arrays.<Object[]>asList(
                o(true, true, "be", "what", "what"),
                o(false, true, "be", "what", "woot"),
                o(true, false, "end with", "what", "at"),
                o(false, false, "end with", "what", "ut"),
                o(true, false, "start with", "what", "wh"),
                o(false, false, "start with", "what", "wu"),
                o(true, true, "match", "what", "w.+"),
                o(false, true, "match", "what", "x.*"),
                o(true, true, "contain", "what", "ha"),
                o(false, true, "contain", "what", "ut")
        );
    }

    private static Object[] o(Object... args) {
        return args;
    }

    private final boolean accepted;
    private final boolean checkNegate;
    private final String mode;
    private final String expected;
    private final String input;

    public StringAssertionsTest(boolean accepted, boolean checkNegate, String mode, String expected, String input) {
        this.accepted = accepted;
        this.checkNegate = checkNegate;
        this.mode = mode;
        this.expected = expected;
        this.input = input;
    }


    @Test
    public void evaluate() {

        if (accepted) {
            StringAssertions.apply(mode, expected, input);
        } else {
            try {
                StringAssertions.apply(mode, expected, input);
                Assertions.fail("Evaluation should have failed");
            } catch (AssertionError e) {
                // ok
            }

        }
    }

    @Test
    public void negate() {
        Assume.assumeTrue(checkNegate);

        StringAssertions.Mode m = StringAssertions.lookupMode(mode).negate();

        if (accepted) {
            try {
                StringAssertions.apply(m, expected, input);
                Assertions.fail("Evaluation should have failed");
            } catch (AssertionError e) {
                // ok
            }
        } else {
            StringAssertions.apply(m, expected, input);
        }
    }

}