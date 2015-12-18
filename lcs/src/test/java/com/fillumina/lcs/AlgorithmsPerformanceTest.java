package com.fillumina.lcs;

import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
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
        config.setBaseIterations(100);
        config.setTimeout(360, TimeUnit.SECONDS);
        config.setMaxStandardDeviation(4);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("Baseline",
                new LcsRunnable(new BaselineLinearSpaceMyersLcs()));
        tests.addTest("MyersLcs",
                new LcsRunnable(new LcsSizeEvaluatorAdaptor(MyersLcs.INSTANCE)));
        tests.addTest("LinearSpaceLcs",
                new LcsRunnable(new LcsSizeEvaluatorAdaptor(LinearSpaceMyersLcs.INSTANCE)));
        tests.addTest("ParallelLinearSpaceMyersLcs",
                new LcsRunnable(new LcsSizeEvaluatorAdaptor(ParallelLinearSpaceMyersLcs.INSTANCE)));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }

}
