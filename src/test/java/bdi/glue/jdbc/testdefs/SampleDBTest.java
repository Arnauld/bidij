package bdi.glue.jdbc.testdefs;

import bdi.TestSettings;
import bdi.glue.testdefs.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleDBTest {

    @Test
    public void usecase() {
        TestSettings settings = new TestSettings();
        SampleDB sampleDB = SampleDB.createFromWorkingDirAndDefaultCredentials(settings.buildDir());
        sampleDB.init();
        User user = sampleDB.get(2);
        assertThat(user).isNotNull();
    }
}