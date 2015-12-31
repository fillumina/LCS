package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

/**
 * Confront the performances of different algorithms. Note that performances
 * can vary for different values of {@link #TOTAL} and {@link #LCS}.
 *
 * @author Francesco Illuminati
 */
public class AlgorithmsPerformanceTest extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 30;
    private static final int LCS = 20;
    private static final long SEED = System.nanoTime();

    private final RandomSequenceGenerator generator;
    private final List<Integer> lcsList;

    public static void main(String[] args) {
        System.out.println("performance evaluation, please wait...");
        new AlgorithmsPerformanceTest().executeWithIntermediateOutput();
    }

    public AlgorithmsPerformanceTest() {
        super();
        this.generator = new RandomSequenceGenerator(TOTAL, LCS, SEED);
        this.lcsList = generator.getLcs();
    }

    private class LcsRunnable implements Runnable {
        private final LcsList lcsAlgorithm;

        public LcsRunnable(LcsList lcsAlgorithm) {
            this.lcsAlgorithm = lcsAlgorithm;
        }

        @Override
        public void run() {
            assertEquals(lcsAlgorithm.getClass().getSimpleName(),
                    lcsList, lcsAlgorithm.lcs(
                            generator.getArrayA(), generator.getArrayB()));
        }
    }

    @Override
    public void init(ProgressionConfigurator config) {
        config.setBaseIterations(100);
        config.setTimeout(10, TimeUnit.MINUTES);
        config.setMaxStandardDeviation(6);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("Baseline",
                new LcsRunnable(new BaselineLinearSpaceMyersLcs()));
        tests.addTest("MyersLcs",
                new LcsRunnable(new LcsLengthAdaptor(MyersLcs.INSTANCE)));
        tests.addTest("LinearSpaceLcs",
                new LcsRunnable(new LcsLengthAdaptor(LinearSpaceMyersLcs.INSTANCE)));
        tests.addTest("HirschbergLcs",
                new LcsRunnable(new LcsLengthAdaptor(HirschbergLinearSpaceLcs.INSTANCE)));
        tests.addTest("WagnerFischerLcs",
                new LcsRunnable(new LcsLengthAdaptor(WagnerFischerLcs.INSTANCE)));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }

}
