package com.fillumina.distance;

import com.fillumina.lcs.*;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class DistancePerformanceTest extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 600;
    private static final int LCS = 400;
    private static final long SEED = System.nanoTime();

    private final List<Integer> lcsList;
    private final List<Integer> a;
    private final List<Integer> b;

    public static void main(String[] args) {
        System.out.println("performance evaluation, please wait...");
        new DistancePerformanceTest().executeWithIntermediateOutput();
    }

    public DistancePerformanceTest() {
        super();
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(TOTAL, LCS, SEED);
        this.lcsList = generator.getLcs();
        this.a = generator.getA();
        this.b = generator.getB();
    }

    @Override
    public void init(ProgressionConfigurator config) {
        config.setBaseIterations(100);
        config.setTimeout(360, TimeUnit.SECONDS);
        config.setMaxStandardDeviation(4);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("StringDistance", new Runnable() {
            @Override
            public void run() {
                assertEquals(2, StringLevenshteinDistance
                        .distance("tuesday", "thursday"));
                assertEquals(5, StringLevenshteinDistance
                        .distance("monday", "saturday"));
            }
        });

        tests.addTest("CharDistance", new Runnable() {
            @Override
            public void run() {
                assertEquals(2, CharLevenshteinDistance
                        .distance("tuesday", "thursday"));
                assertEquals(5, CharLevenshteinDistance
                        .distance("monday", "saturday"));
            }
        });

        tests.addTest("MyersDistance", new Runnable() {
            @Override
            public void run() {
                assertEquals(3, MyersDistance
                        .distance("tuesday", "thursday"));
                assertEquals(8, MyersDistance
                        .distance("monday", "saturday"));
            }
        });

    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }

}
