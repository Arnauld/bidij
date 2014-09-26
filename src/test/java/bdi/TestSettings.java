package bdi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TestSettings {

    private static final Charset UTF8 = Charset.forName("utf8");

    private Properties properties;

    public TestSettings() {
    }

    public String getProperty(String key) {
        return properties().getProperty(key);
    }

    private Properties properties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream in = TestSettings.class.getResourceAsStream("/test-settings.properties")) {
                properties.load(new InputStreamReader(in, UTF8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties;
    }

    public String projectDir() {
        return getProperty("projectDir");
    }

    public String buildDir() {
        return getProperty("buildDir");
    }
}
