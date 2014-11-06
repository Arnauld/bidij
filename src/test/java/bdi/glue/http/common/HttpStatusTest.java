package bdi.glue.http.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HttpStatusTest {

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.<Object[]>asList(
                o(HttpStatus.CONTINUE, HttpStatus.Series.INFORMATIONAL),
                o(HttpStatus.ACCEPTED, HttpStatus.Series.SUCCESSFUL),
                o(HttpStatus.MOVED_PERMANENTLY, HttpStatus.Series.REDIRECTION),
                o(HttpStatus.UNAUTHORIZED, HttpStatus.Series.CLIENT_ERROR),
                o(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.Series.SERVER_ERROR)
        );
    }

    private static Object[] o(Object... args) {
        return args;
    }

    private final HttpStatus httpStatus;
    private final HttpStatus.Series series;

    public HttpStatusTest(HttpStatus httpStatus, HttpStatus.Series series) {
        this.httpStatus = httpStatus;
        this.series = series;
    }

    @Test
    public void checkSeries() {
        assertThat(httpStatus.series()).isEqualTo(series);
    }
}