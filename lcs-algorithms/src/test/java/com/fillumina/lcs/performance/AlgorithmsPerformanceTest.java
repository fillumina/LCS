package com.fillumina.lcs.performance;

import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.hirschberg.HirschbergLinearSpaceAlgorithmLcs;
import com.fillumina.lcs.hirschberg.OptimizedHirschbergLinearSpaceLcs;
import com.fillumina.lcs.myers.MyersLcs;
import com.fillumina.lcs.myers.OptimizedMyersLcs;
import com.fillumina.lcs.myers.RLinearSpaceMyersLcs;
import com.fillumina.lcs.myers.ReverseMyersLcs;
import com.fillumina.lcs.recursive.MemoizedRecursiveLcs;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import static org.junit.Assert.assertEquals;
import com.fillumina.lcs.scoretable.BottomUpLcs;
import com.fillumina.lcs.scoretable.SmithWatermanLcs;
import com.fillumina.lcs.scoretable.WagnerFischerLcs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
// TODO do a performance test with all the myers implementation (including mine)
public class AlgorithmsPerformanceTest extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 60;
    private static final int LCS = 40;
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
        config.setTimeout(30, TimeUnit.MINUTES);
        config.setMaxStandardDeviation(2);
    }

    @Override
    public void addTests(TestContainer tests) {
        // these are WAY slower than anything else (always avoid recursing)!
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