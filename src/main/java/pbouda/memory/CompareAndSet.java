package pbouda.memory;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

public class CompareAndSet {

    @JCStressTest
    @Outcome(id = "1, 1", expect = ACCEPTABLE)
    @Outcome(id = "0, 1", expect = ACCEPTABLE)
    @Outcome(id = "1, 0", expect = FORBIDDEN)
    @State
    public static class test {

        boolean cancel;
        AtomicBoolean suspended = new AtomicBoolean(true);

        @Actor
        public void actor1(II_Result r) {
            cancel = true;
            suspended.compareAndSet(true, false);
            r.r2 = 1;
        }

        @Actor
        public void actor2(II_Result r) {
            suspended.compareAndSet(false, true);
            if (cancel) {
                r.r1 = 1;
            } else {
                r.r1 = 0;
            }
        }
    }
}