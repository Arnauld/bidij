package bdi.glue.util;

import org.junit.Test;

import static bdi.glue.util.StringAssertions.Mode.*;
import static bdi.glue.util.StringAssertions.lookupMode;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StringAssertionsModeTest {

    @Test
    public void lookup() {
        assertThat(lookupMode("Ends with")).isEqualTo(END_WITH);
        assertThat(lookupMode("End With")).isEqualTo(END_WITH);
        assertThat(lookupMode("starts with")).isEqualTo(START_WITH);
        assertThat(lookupMode("Start With")).isEqualTo(START_WITH);
        assertThat(lookupMode("Match")).isEqualTo(MATCH);
        assertThat(lookupMode("Matches")).isEqualTo(MATCH);
        assertThat(lookupMode("Be")).isEqualTo(BE);
        assertThat(lookupMode("Is Equal To")).isEqualTo(BE);
        assertThat(lookupMode("Same As")).isEqualTo(BE);
        assertThat(lookupMode("Contain")).isEqualTo(CONTAIN);
        assertThat(lookupMode("Contains")).isEqualTo(CONTAIN);
        //
        assertThat(lookupMode("beuarhh")).isNull();
    }
    @Test
    public void negate() {
        assertThat(END_WITH.negate()).isEqualTo(NOT_END_WITH);
        assertThat(START_WITH.negate()).isEqualTo(NOT_START_WITH);
        assertThat(BE.negate()).isEqualTo(NOT_BE);
        assertThat(CONTAIN.negate()).isEqualTo(NOT_CONTAIN);
        assertThat(MATCH.negate()).isEqualTo(NOT_MATCH);
        assertThat(NOT_END_WITH.negate()).isEqualTo(END_WITH);
        assertThat(NOT_START_WITH.negate()).isEqualTo(START_WITH);
        assertThat(NOT_BE.negate()).isEqualTo(BE);
        assertThat(NOT_CONTAIN.negate()).isEqualTo(CONTAIN);
        assertThat(NOT_MATCH.negate()).isEqualTo(MATCH);
    }
}
