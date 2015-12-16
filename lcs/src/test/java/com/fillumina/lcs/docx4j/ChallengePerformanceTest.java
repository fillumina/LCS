package com.fillumina.lcs.docx4j;

import com.fillumina.lcs.CollectionLcsInput;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.LcsInput;
import com.fillumina.lcs.LcsLengthSequence;
import com.fillumina.lcs.LcsSequencer;
import com.fillumina.lcs.LcsSizeEvaluatorAdaptor;
import com.fillumina.lcs.LinearSpaceMyersLcs;
import com.fillumina.lcs.ListLcs;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 * A performance test that compares the LCS implementation of this project
 * with the one present in IBM DOCX4J.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ChallengePerformanceTest
        extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 600;
    private static final int LCS = 50;
    private static final long SEED = System.nanoTime();

    private final List<Integer> lcsList;
    private final List<Integer> a;
    private final List<Integer> b;

    public static void main(String[] args) {
        new ChallengePerformanceTest().executeWithIntermediateOutput();
    }

    public ChallengePerformanceTest() {
        super();
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(TOTAL, LCS, SEED);
        this.lcsList = generator.getLcs();
        this.a = generator.getA();
        this.b = generator.getB();
        assertEquals(TOTAL, this.a.size());
        assertEquals(TOTAL, this.b.size());
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
                final IbmLcsLength ibmLcsLength = new IbmLcsLength(a, b);
                assertEquals(LCS, ibmLcsLength.getLcs());
            }
        });

        tests.addTest("mine", new Runnable() {

            @Override
            public void run() {
                LcsInput input = new CollectionLcsInput<>(a, b);
                LcsLengthSequence seq = new LcsLengthSequence();
                new LinearSpaceMyersLcs().calculateLcs(input, seq);
                assertEquals(LCS, seq.getLcsLength());
            }
        });
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }
}
