package com.fillumina.lcs.algorithm.performance;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.algorithm.hirschberg.HirschbergLinearSpaceAlgorithmLcs;
import com.fillumina.lcs.algorithm.hirschberg.OptimizedHirschbergLinearSpaceLcs;
import com.fillumina.lcs.algorithm.myers.MyersLcs;
import com.fillumina.lcs.algorithm.myers.OptimizedMyersLcs;
import com.fillumina.lcs.algorithm.myers.ReverseMyersLcs;
import com.fillumina.lcs.algorithm.myers.linearspace.RLinearSpaceMyersLcs;
import com.fillumina.lcs.algorithm.scoretable.BottomUpLcs;
import com.fillumina.lcs.algorithm.scoretable.SmithWatermanLcs;
import com.fillumina.lcs.algorithm.scoretable.WagnerFischerLcs;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati
 */
public class AlgorithmsPerformanceTest extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 10;
    private static final int LCS = 7;
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
        config.setBaseIterations(1_000);
        config.setTimeout(360, TimeUnit.SECONDS);
        config.setMaxStandardDeviation(2);
    }

    @Override
    public void addTests(TestContainer tests) {
        // Recursive is WAY slower than anything else
//        tests.addTest("Recursive", new LcsRunnable(new RecursiveLcs()));
//        tests.addTest("MemoizedRecursive",
//                new LcsRunnable(new MemoizedRecursiveLcs()));

        tests.addTest("BottomUp", new LcsRunnable(new BottomUpLcs()));
        tests.addTest("SmithWaterman",
                new LcsRunnable(new SmithWatermanLcs()));
        tests.addTest("WagnerFischer",
                new LcsRunnable(new WagnerFischerLcs()));


        tests.addTest("HirschbergLinearSpaceAlgorithm",
                new LcsRunnable(new HirschbergLinearSpaceAlgorithmLcs()));
        tests.addTest("OptimizedHirschbergLinearSpace",
                new LcsRunnable(new OptimizedHirschbergLinearSpaceLcs()));

        tests.addTest("Myers", new LcsRunnable(new MyersLcs()));
        tests.addTest("ReverseMyers", new LcsRunnable(new ReverseMyersLcs()));
        tests.addTest("OptimizedMyers", new LcsRunnable(new OptimizedMyersLcs()));
        tests.addTest("RLinearSpaceMyers",
                new LcsRunnable(new RLinearSpaceMyersLcs()));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }
}
