package com.fillumina.lcs.algorithm.performance;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.suite.ParameterContainer;
import com.fillumina.performance.producer.suite.ParametrizedSequenceRunnable;
import com.fillumina.performance.producer.suite.SequenceContainer;
import com.fillumina.performance.template.AssertionSuiteBuilder;
import com.fillumina.performance.template.ParametrizedSequencePerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ComparativePerformanceTest
        extends ParametrizedSequencePerformanceTemplate<LcsList, int[]> {

    @Override
    public void init(ProgressionConfigurator config) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addParameters(ParameterContainer<LcsList> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addSequence(
            SequenceContainer<?, int[]> sequences) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ParametrizedSequenceRunnable<LcsList, int[]> getTest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addAssertions(AssertionSuiteBuilder assertionBuilder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addIntermediateAssertions(PerformanceAssertion assertion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
