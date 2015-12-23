package com.fillumina.lcs.algorithm.performance;

import com.fillumina.lcs.algorithm.hirschberg.HirschbergLinearSpaceAlgorithmLcs;
import com.fillumina.lcs.algorithm.hirschberg.OptimizedHirschbergLinearSpaceLcs;
import com.fillumina.lcs.algorithm.myers.MyersLcs;
import com.fillumina.lcs.algorithm.myers.OptimizedMyersLcs;
import com.fillumina.lcs.algorithm.myers.ReverseMyersLcs;
import com.fillumina.lcs.algorithm.myers.linearspace.RLinearSpaceMyersLcs;
import com.fillumina.lcs.algorithm.scoretable.BottomUpLcs;
import com.fillumina.lcs.algorithm.scoretable.SmithWatermanLcs;
import com.fillumina.lcs.algorithm.scoretable.WagnerFischerLcs;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import com.fillumina.performance.consumer.assertion.SuiteExecutionAssertion;
import com.fillumina.performance.producer.suite.ParameterContainer;
import com.fillumina.performance.producer.suite.ParametrizedExecutor;
import com.fillumina.performance.producer.suite.ParametrizedRunnable;
import com.fillumina.performance.template.ParametrizedPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati
 */
public class AlgorithmsPerformanceTest
        extends ParametrizedPerformanceTemplate<LcsList> {

    private static final int TOTAL = 40;
    private static final int LCS = 3;
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

    @Override
    public void init(ProgressionConfigurator config) {
        config.setBaseIterations(1_000);
        config.setTimeout(360, TimeUnit.SECONDS);
        config.setMaxStandardDeviation(3);
    }

    @Override
    public void addParameters(ParameterContainer<LcsList> parameters) {
        // Recursive is WAY slower than anything else
//        parameters.addParameter("Recursive", new RecursiveLcs());
//        parameters.addParameter("MemoizedRecursive", new MemoizedRecursiveLcs());

        parameters.addParameter("BottomUp", new BottomUpLcs());
        parameters.addParameter("SmithWaterman", new SmithWatermanLcs());
        parameters.addParameter("WagnerFischer", new WagnerFischerLcs());


        parameters.addParameter("HirschbergLinearSpaceAlgorithm",
                new HirschbergLinearSpaceAlgorithmLcs());
        parameters.addParameter("OptimizedHirschbergLinearSpace",
                new OptimizedHirschbergLinearSpaceLcs());

        parameters.addParameter("Myers", new MyersLcs());
        parameters.addParameter("ReverseMyers", new ReverseMyersLcs());
        parameters.addParameter("OptimizedMyers", new OptimizedMyersLcs());
        parameters.addParameter("RLinearSpaceMyers", new RLinearSpaceMyersLcs());

//        parameters.addParameter("OptimizedMyersLcs",
//                new LcsLengthAdaptor(com.fillumina.lcs.MyersLcs.INSTANCE));
//        parameters.addParameter("OptimizedLinearSpaceLcs",
//                new LcsLengthAdaptor(LinearSpaceMyersLcs.INSTANCE));

    }

    @Override
    public void executeTests(ParametrizedExecutor<LcsList> executor) {
        executor.executeTest(new ParametrizedRunnable<LcsList>() {
            @Override
            public void call(LcsList lcs) {
                assertEquals(lcs.getClass().getSimpleName(),
                        lcsList,
                        lcs.lcs(generator.getArrayA(), generator.getArrayB()));
            }
        });
    }

    @Override
    public void addAssertions(SuiteExecutionAssertion assertion) {
    }
}
