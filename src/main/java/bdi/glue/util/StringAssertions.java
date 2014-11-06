package bdi.glue.util;

import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StringAssertions {

    public enum Mode {
        MATCH((s, input) -> assertThat(s).matches(input)),
        PARTIALLY_MATCH((s, input) -> {
            Pattern p = Pattern.compile(input);
            assertThat(p.matcher(s).find()).isTrue();
        }),
        CONTAIN((s, input) -> assertThat(s).contains(input)),
        BE((s, input) -> assertThat(s).isEqualTo(input)),
        START_WITH((s, input) -> assertThat(s).startsWith(input)),
        END_WITH((s, input) -> assertThat(s).endsWith(input)),
        NOT_MATCH((s, input) -> assertThat(s).doesNotMatch(input)),
        NOT_CONTAIN((s, input) -> assertThat(s).doesNotContain(input)),
        NOT_BE((s, input) -> assertThat(s).isNotEqualTo(input)),
        NOT_START_WITH((s, input) -> assertThat(s.startsWith(input)).isFalse()),
        NOT_END_WITH((s, input) -> assertThat(s.endsWith(input)).isFalse());

        private BiConsumer<String, String> applier;

        Mode(BiConsumer<String, String> applier) {
            this.applier = applier;
        }

        public BiConsumer<String, String> applier() {
            return applier;
        }

        public Mode negate() {
            switch (this) {
                case MATCH:
                    return NOT_MATCH;
                case CONTAIN:
                    return NOT_CONTAIN;
                case BE:
                    return NOT_BE;
                case START_WITH:
                    return NOT_START_WITH;
                case END_WITH:
                    return NOT_END_WITH;
                case NOT_MATCH:
                    return MATCH;
                case NOT_CONTAIN:
                    return CONTAIN;
                case NOT_BE:
                    return BE;
                case NOT_START_WITH:
                    return START_WITH;
                case NOT_END_WITH:
                    return END_WITH;
                default:
                    throw new IllegalArgumentException("No negation supported for " + this);
            }
        }
    }

    public static void apply(String mode, String actual, String expected) {
        Mode m = lookupMode(mode);
        assertThat(m).describedAs("No mode matching '" + mode + "'").isNotNull();
        apply(m, actual, expected);
    }

    public static void apply(Mode mode, String actual, String expected) {
        mode.applier().accept(actual, expected);
    }

    public static Mode lookupMode(String mode) {
        switch (mode.toLowerCase()) {
            case "match":
            case "matches":
            case "satisfy":
            case "satisfies":
                return Mode.MATCH;
            case "partially match":
            case "partially matches":
            case "partially satisfy":
            case "partially satisfies":
                return Mode.PARTIALLY_MATCH;
            case "contain":
            case "contains":
                return Mode.CONTAIN;
            case "be":
            case "is equal to":
            case "equal to":
            case "identical to":
            case "same as":
                return Mode.BE;
            case "start with":
            case "starts with":
                return Mode.START_WITH;
            case "end with":
            case "ends with":
                return Mode.END_WITH;
        }
        return null;
    }
}
