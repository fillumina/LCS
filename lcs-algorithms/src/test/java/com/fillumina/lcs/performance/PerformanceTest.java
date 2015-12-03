package com.fillumina.lcs.performance;

import com.fillumina.lcs.LinearSpaceMyersLcsAdaptor;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class PerformanceTest extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 600;
    private static final int LCS = 500;
    private static final long SEED = System.nanoTime();

    private final List<Integer> lcsList;
    private final List<Integer> a;
    private final List<Integer> b;

    public static void main(String[] args) {
        new PerformanceTest().executeWithIntermediateOutput();
    }

    public PerformanceTest() {
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

//        tests.addTest("BottomUp", new LcsRunnable(new BottomUpLcs()));
//        tests.addTest("OptimizedHirschbergLinearSpace",
//                new LcsRunnable(new OptimizedHirschbergLinearSpaceLcs()));
//        tests.addTest("HirschbergLinearSpaceAlgorithm",
//                new LcsRunnable(new HirschbergLinearSpaceAlgorithmLcs()));
//        tests.addTest("MemoizedRecursive",
//                new LcsRunnable(new MemoizedRecursiveLcs()));
//        tests.addTest("NotRecursive", new LcsRunnable(new NotRecursiveLcs()));
//        tests.addTest("OptimizedRecursive",
//                new LcsRunnable(new OptimizedRecursiveLcs()));
//        tests.addTest("WagnerFischer",
//                new LcsRunnable(new WagnerFischerLcs()));
//        tests.addTest("SmithWaterman",
//                new LcsRunnable(new SmithWatermanLcs()));


//        tests.addTest("Myers", new LcsRunnable(new MyersLcs()));
//        tests.addTest("ReverseMyers", new LcsRunnable(new ReverseMyersLcs()));
//        tests.addTest("OptimizedMyers", new LcsRunnable(new OptimizedMyersLcs()));
//        tests.addTest("RLinearSpaceMyers",
//                new LcsRunnable(new RLinearSpaceMyersLcs()));
//        tests.addTest("AbstractMyers",
//                new LcsRunnable(new AbstractMyersLcsAdaptor()));
//        tests.addTest("BaselineOptimizedLinearSpaceMyers",
//                new LcsRunnable(new BaselineOptimizedLinearSpaceMyersLcs()));
//        tests.addTest("BaselineLinearSpaceMyers",
//                new LcsRunnable(new HyperOptimizedLinearSpaceMyersLcsAdaptor()));
        tests.addTest("LinearSpaceMyers",
                new LcsRunnable(new LinearSpaceMyersLcsAdaptor()));
//        test.addTest("ParallelLinearSpaceMyers",
//                new LcsRunnable(new ParallelLinearSpaceMyersLcsHelper()));
//        tests.addTest("Ibm", new LcsRunnable(new IbmLcs()));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }
}
