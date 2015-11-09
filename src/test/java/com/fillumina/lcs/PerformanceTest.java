package com.fillumina.lcs;

import com.fillumina.lcs.myers.ALinearSpaceMyersLcs;
import com.fillumina.lcs.myers.MyersLcs;
import com.fillumina.lcs.myers.docx4j.IbmLcs;
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

    private static final int TOTAL = 100;
    private static final int LCS = 10;
    private static final long SEED = System.currentTimeMillis();

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
        private final Lcs<Integer> lcsAlgorithm;

        public LcsRunnable(Lcs<Integer> lcsAlgorithm) {
            this.lcsAlgorithm = lcsAlgorithm;
        }

        @Override
        public void run() {
            assertEquals(lcsList, lcsAlgorithm.lcs(a, b));
        }
    }

    @Override
    public void init(ProgressionConfigurator config) {
        config.setBaseIterations(10);
        config.setTimeout(1, TimeUnit.MINUTES);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("BottomUp", new LcsRunnable(new BottomUpLcs<Integer>()));
//        tests.addTest("DirectHirschbergLinearSpace",
//                new LcsRunnable(new DirectHirschbergLinearSpaceLcs<Integer>()));
//        tests.addTest("HirschbergLinearSpaceAlgorithm",
//                new LcsRunnable(new HirschbergLinearSpaceAlgorithmLcs<Integer>()));
//        tests.addTest("MemoizedRecursive",
//                new LcsRunnable(new MemoizedRecursiveLcs<Integer>()));
//        tests.addTest("NotRecursive", new LcsRunnable(new NotRecursiveLcs<Integer>()));
//        tests.addTest("OptimizedRecursive",
//                new LcsRunnable(new OptimizedRecursiveLcs<Integer>()));
//        tests.addTest("Recursive", new LcsRunnable(new RecursiveLcs<Integer>()));
//        tests.addTest("WagnerFischer",
//                new LcsRunnable(new WagnerFischerLcs<Integer>()));
//        tests.addTest("SpaceOptimizedMyers",
//                new LcsRunnable(new SpaceOptimizedMyersLcs<Integer>()));
        tests.addTest("Myers", new LcsRunnable(new MyersLcs<Integer>()));
//        tests.addTest("ReverseMyers", new LcsRunnable(new ReverseMyersLcs<Integer>()));
//        tests.addTest("RLinearSpaceMyers",
//                new LcsRunnable(new RLinearSpaceMyersLcs<Integer>()));
//        tests.addTest("MLinearSpaceMyers",
//                new LcsRunnable(new MLinearSpaceMyersLcs<Integer>()));
        tests.addTest("ALinearSpaceMyers",
                new LcsRunnable(new ALinearSpaceMyersLcs<Integer>()));
        tests.addTest("Ibm", new LcsRunnable(new IbmLcs<Integer>()));
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }
}