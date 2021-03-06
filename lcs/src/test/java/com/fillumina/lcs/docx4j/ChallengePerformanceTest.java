package com.fillumina.lcs.docx4j;

import com.fillumina.lcs.BaselineLinearSpaceMyersLcs;
import com.fillumina.lcs.LinearSpaceMyersLcs;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

/**
 * A performance test that compares the LCS implementation from this project
 * with the one present in IBM DOCX4J.
 *
 * @author Francesco Illuminati
 */
public class ChallengePerformanceTest
        extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 600;
    private static final int LCS = 40;
    private static final long SEED = System.nanoTime();

    private final RandomSequenceGenerator generator;
    private final List<Integer> lcsList;

    public static void main(String[] args) {
        new ChallengePerformanceTest().executeWithIntermediateOutput();
    }

    public ChallengePerformanceTest() {
        super();
        this.generator = new RandomSequenceGenerator(TOTAL, LCS, SEED);
        this.lcsList = generator.getLcs();
        assertEquals(TOTAL, generator.getArrayA().length);
        assertEquals(TOTAL, generator.getArrayB().length);
        assertEquals(LCS, this.lcsList.size());
    }

    @Override
    public void init(ProgressionConfigurator config) {
        config.setBaseIterations(100);
        config.setTimeout(30, TimeUnit.MINUTES);
        config.setMaxStandardDeviation(2);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("IBM", new Runnable() {
            @Override
            public void run() {
                final IbmLcsLength ibmLcsLength =
                        new IbmLcsLength(generator.getArrayA(),
                                generator.getArrayB());
                assertEquals(LCS, ibmLcsLength.getLcs());
            }
        });

        tests.addTest("mine", new Runnable() {
            @Override
            public void run() {
                assertEquals(LCS, LinearSpaceMyersLcs.INSTANCE
                        .calculateLcsLength(
                                generator.getArrayA(), generator.getArrayB()));
            }
        });

        tests.addTest("baseline", new Runnable() {
            @Override
            public void run() {
                assertEquals(LCS,  new BaselineLinearSpaceMyersLcs()
                        .lcs(generator.getArrayA(), generator.getArrayB()).size());
            }
        });
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }
}
