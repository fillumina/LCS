package com.fillumina.lcs.myers;

import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.LcsSizeEvaluator;
import com.fillumina.lcs.LinearSpaceMyersLcs;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
// TODO do a performance test with all the myers implementation (including mine)
public class AlgorithmsPerformanceTest extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 600;
    private static final int LCS = 400;
    private static final long SEED = System.nanoTime();

    private final List<Integer> lcsList;
    private final List<Integer> a;
    private final List<Integer> b;

    public static void main(String[] args) {
        System.out.println("performance evaluation, please wait...");
        new AlgorithmsPerformanceTest().executeWithIntermediateOutput();
    }

    public AlgorithmsPerformanceTest() {
        super();
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(TOTAL, LCS, SEED);
        this.lcsList = generator.getLcs();
        this.a = generator.getA();
        this.b = generator.getB();
    }

    private class LcsRunnable implements Runnable {
        private final Lcs lcsAlgorithm;

        public LcsRunnable(Lcs lcsAlgorithm) {
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
        config.setBaseIterations(100);
        config.setTimeout(360, TimeUnit.SECONDS);
        config.setMaxStandardDeviation(7);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("ForwardMyersLcs",
                new LcsRunnable(new AbstractMyersLcsAdaptor()));
        tests.addTest("HyperOptimizedLinearSpaceMyersLcs",
                new LcsRunnable(new HyperOptimizedLinearSpaceMyersLcsAdaptor()));
        tests.addTest("ParallelLinearSpaceMyersLcs",
                new LcsRunnable(new AbstractParallelLinearSpaceMyersLcsAdaptor()));
        tests.addTest("LinearSpaceLcs",
                new LcsRunnable(new LinearSpaceMyersLcsListAdaptor()));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }

    private static class LinearSpaceMyersLcsListAdaptor
            implements LcsSizeEvaluator {
        private LinearSpaceMyersLcs<?> linearSpaceMyersLcs;

        @Override
        public int getLcsSize() {
            return linearSpaceMyersLcs.getLcsLength();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
            linearSpaceMyersLcs = LinearSpaceMyersLcs.lcs(a, b);
            return (List<? extends T>) linearSpaceMyersLcs.extractLcsList();
        }
    }
}
