package com.fillumina.lcs.testutil;

import com.fillumina.lcs.ListLcs;
import com.fillumina.lcs.LinearSpaceMyersLcsWrapper;
import com.fillumina.lcs.myers.OptimizedMyersLcs;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
        this.lcsList = generator.getLcsList();
        this.a = generator.getA();
        this.b = generator.getB();
    }

    private class LcsRunnable implements Runnable {
        private final ListLcs<Integer> lcsAlgorithm;

        public LcsRunnable(ListLcs<Integer> lcsAlgorithm) {
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
        config.setBaseIterations(500);
        config.setTimeout(10, TimeUnit.MINUTES);
    }

    @Override
    public void addTests(TestContainer tests) {

//        tests.addTest("BottomUp", new LcsRunnable(new BottomUpLcs<Integer>()));
//        tests.addTest("OptimizedHirschbergLinearSpace",
//                new LcsRunnable(new OptimizedHirschbergLinearSpaceLcs<Integer>()));
//        tests.addTest("HirschbergLinearSpaceAlgorithm",
//                new LcsRunnable(new HirschbergLinearSpaceAlgorithmLcs<Integer>()));
//        tests.addTest("MemoizedRecursive",
//                new LcsRunnable(new MemoizedRecursiveLcs<Integer>()));
//        tests.addTest("NotRecursive", new LcsRunnable(new NotRecursiveLcs<Integer>()));
//        tests.addTest("OptimizedRecursive",
//                new LcsRunnable(new OptimizedRecursiveLcs<Integer>()));
//        tests.addTest("WagnerFischer",
//                new LcsRunnable(new WagnerFischerLcs<Integer>()));
//        tests.addTest("SmithWaterman",
//                new LcsRunnable(new SmithWatermanLcs<Integer>()));


//        tests.addTest("Myers", new LcsRunnable(new MyersLcs<Integer>()));
//        tests.addTest("ReverseMyers", new LcsRunnable(new ReverseMyersLcs<Integer>()));
        tests.addTest("OptimizedMyers", new LcsRunnable(new OptimizedMyersLcs<Integer>()));
//        tests.addTest("RLinearSpaceMyers",
//                new LcsRunnable(new RLinearSpaceMyersLcs<Integer>()));
//        tests.addTest("BaselineOptimizedLinearSpaceMyers",
//                new LcsRunnable(new BaselineOptimizedLinearSpaceMyersLcs<Integer>()));
        tests.addTest("LinearSpaceMyers",
                new LcsRunnable(new LinearSpaceMyersLcsWrapper<Integer>()));
//        tests.addTest("ParallelLinearSpaceMyers",
//                new LcsRunnable(new ParallelLinearSpaceMyersLcsHelper<Integer>()));
//        tests.addTest("Ibm", new LcsRunnable(new IbmLcs<Integer>()));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }
}