package com.fillumina.distance;

import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

/**
 * Tests the performances of two different min algorithms.
 * 
 * @author Francesco Illuminati 
 */
public class MinAlgorithmPerformanceTest extends AutoProgressionPerformanceTemplate {

    public static void main(String[] args) {
        System.out.println("performance evaluation, please wait...");
        new MinAlgorithmPerformanceTest().executeWithIntermediateOutput();
    }

    @Override
    public void init(ProgressionConfigurator config) {
        config.setBaseIterations(10_000);
        config.setTimeout(360, TimeUnit.SECONDS);
        config.setMaxStandardDeviation(2);
    }

    @Override
    public void addTests(TestContainer tests) {
        tests.addTest("min1", new Runnable() {
            @Override
            public void run() {
                assertEquals(1, min1(1, 2, 3));
                assertEquals(1, min1(2, 1, 3));
                assertEquals(1, min1(3, 2, 1));
                assertEquals(1, min1(1, 3, 2));
                assertEquals(1, min1(1, 3, 1));
                assertEquals(1, min1(1, 1, 2));
                assertEquals(1, min1(1, 1, 1));
                assertEquals(1, min1(2, 1, 1));
            }
        });

        tests.addTest("min2", new Runnable() {
            @Override
            public void run() {
                assertEquals(1, min2(1, 2, 3));
                assertEquals(1, min2(2, 1, 3));
                assertEquals(1, min2(3, 2, 1));
                assertEquals(1, min2(1, 3, 2));
                assertEquals(1, min2(1, 3, 1));
                assertEquals(1, min2(1, 1, 2));
                assertEquals(1, min2(1, 1, 1));
                assertEquals(1, min2(2, 1, 1));
            }
        });
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }

    static int min1(int a, int b, int c) {
        if (a < b) {
            if (c < a) {
                return c;
            }
            return a;
        } else {
            if (c < b) {
                return c;
            }
            return b;
        }
    }

    static int min2(int a, int b, int c) {
        int mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;
    }

}
