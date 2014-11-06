package bdi.junit;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to provides options for the Parallel execution of tests.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 * @see bdi.junit.ParallelRunner
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface ParallelOptions {
    int concurrency() default -1;

    long terminationTimeout() default 1;

    TimeUnit terminationTimeoutUnit() default TimeUnit.MINUTES;
}