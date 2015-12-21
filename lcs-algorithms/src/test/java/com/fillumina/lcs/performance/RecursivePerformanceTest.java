package com.fillumina.lcs.performance;

import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fillumina.lcs.recursive.MemoizedRecursiveLcs;
import com.fillumina.lcs.recursive.RecursiveLcs;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import static org.junit.Assert.assertEquals;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
// TODO do a performance test with all the myers implementation (including mine)
public class RecursivePerformanceTest extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 15;
    private static final int LCS = 10;
    private static final long SEED = System.nanoTime();

    private final List<Integer> lcsList;
    private final List<Integer> a;
    private final List<Integer> b;

    public static void main(String[] args) {
        System.out.println("performance evaluation, please wait...");
        new RecursivePerformanceTest().executeWithIntermediateOutput();
    }

    public RecursivePerformanceTest() {
        super();
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(TOTAL, LCS, SEED);
        this.lcsList = generator.getLcs();
        this.a = generator.getA();
        this.b = generator.getB();
    }

    private class LcsRunnable implements Runnable {
        private final LcsList lcsAlgorithm;

        public LcsRunnable(LcsList lcsAlgorithm) {
            this.lcsAlgorithm = lcsAlgorithm;
        }

        @Override
        public void run() {
            assertEquals(lcsAlgorithm.getClass().getSimpleName(),
                    lcsList, lcsAlgorithm.lcs(a, b));
        }
    }

    @Override
    public void init(ProgressionConfigurator config) {
        config.setBaseIterations(30);
        config.setTimeout(30, TimeUnit.MINUTES);
        config.setMaxStandardDeviation(2);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("Recursive", new LcsRunnable(new RecursiveLcs()));
        tests.addTest("MemoizedRecursive",
                new LcsRunnable(new MemoizedRecursiveLcs()));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }
}
