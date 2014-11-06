package bdi.junit;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class ParallelSchedulerTest {

    private static ConcurrentLinkedQueue<String> out = new ConcurrentLinkedQueue<>();

    @Test
    public void testcase_should_launch_in_dedicated_thread() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(AnnotatedWithParallel.class);

        assertThat(result.getFailures()).isEmpty();
        assertThat(result.getRunCount()).isEqualTo(3);
        assertThat(out).containsExactly("case1.1", "case2", "case3.1", "case1.2", "case3.2");
    }

    @RunWith(ParallelRunner.class)
    @ParallelOptions(concurrency = 4)
    public static class AnnotatedWithParallel {

        @Test
        public void case1() throws InterruptedException {
            out("case1.1");
            Thread.sleep(300);
            out("case1.2");
        }

        @Test
        public void case2() throws InterruptedException {
            Thread.sleep(100);
            out("case2");
        }

        @Test
        public void case3() throws InterruptedException {
            Thread.sleep(200);
            out("case3.1");
            Thread.sleep(400);
            out("case3.2");
        }

        private void out(String s) {
            out.add(s);
        }

    }
}