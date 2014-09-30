package bdi.misc;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RegexTest {

    @Test
    public void case01_embedded_multiline_mode() {
        String text = "" +
                "total 0\n" +
                "drwxr-xr-x   3 Arnauld  staff   102 Sep 30 19:34 .\n" +
                "drwxr-xr-x  78 Arnauld  staff  2652 Sep 30 19:34 ..\n" +
                "-rw-r--r--   1 Arnauld  staff     0 Sep 30 19:34 filemode.file\n";
        Pattern p = Pattern.compile("\\Q-rw-r--r--\\E (.+) filemode\\.file");
        Matcher matcher = p.matcher(text);
        assertThat(matcher.find()).isTrue();
    }
}
