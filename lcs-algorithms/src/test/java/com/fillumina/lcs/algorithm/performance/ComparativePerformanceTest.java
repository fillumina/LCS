package com.fillumina.lcs.algorithm.performance;

import com.fillumina.lcs.LinearSpaceMyersLcs;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.suite.ParameterContainer;
import com.fillumina.performance.producer.suite.ParametrizedSequenceRunnable;
import com.fillumina.performance.producer.suite.SequenceContainer;
import com.fillumina.performance.template.AssertionSuiteBuilder;
import com.fillumina.performance.template.ParametrizedSequencePerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ComparativePerformanceTest
        extends ParametrizedSequencePerformanceTemplate<LcsList, int[]> {

    public static void main(String[] args) {
        new ComparativePerformanceTest().executeWithOutput();
    }

    @Override
    public void init(ProgressionConfigurator config) {
        config.setMaxStandardDeviation(4)
                .setTimeout(1, TimeUnit.MINUTES);
    }

    @Override
    public void addParameters(ParameterContainer<LcsList> parameters) {
        parameters.addParameter("OptimizedMyersLcs",
                new LcsLengthAdaptor(com.fillumina.lcs.MyersLcs.INSTANCE));
        parameters.addParameter("OptimizedLinearSpaceLcs",
                new LcsLengthAdaptor(LinearSpaceMyersLcs.INSTANCE));
    }

    @Override
    public void addSequence(SequenceContainer<?, int[]> sequences) {
        sequences.setSequence(
                new int[]{10, 7},
                new int[]{600, 400});
    }

    @Override
    protected ParametrizedSequenceRunnable<LcsList, int[]> getTest() {

        return new ParametrizedSequenceRunnable<LcsList, int[]>() {
            @Override
            public Object sink(LcsList lcsList, int[] totalLcs) {
                RandomSequenceGenerator gen =
                        new RandomSequenceGenerator(totalLcs[0], totalLcs[1]);
                return lcsList.lcs(gen.getArrayA(), gen.getArrayB());
            }
        };
    }

    @Override
    public void addAssertions(AssertionSuiteBuilder assertionBuilder) {
    }

    @Override
    public void addIntermediateAssertions(PerformanceAssertion assertion) {
    }
}
