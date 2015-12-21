package com.fillumina.lcs.performance;

import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;

/**
 * Performance test to check the penalty of using lists against arrays.
 * Reading an arrays is around 15% faster than reading a list.
 *
 * @author Francesco Illuminati 
 */
public class ArrayVsListPerformanceTest
        extends AutoProgressionPerformanceTemplate {
    private final static int SIZE = 100;

    public static void main(String[] args) {
        new ArrayVsListPerformanceTest().executeWithIntermediateOutput();
    }

    @Override
    public void init(ProgressionConfigurator config) {
    }

    @Override
    public void addTests(TestContainer tests) {
        final List<Integer> list = new ArrayList<>(SIZE);
        final int[] array = new int[SIZE];
        for (int i=0; i<SIZE; i++) {
            array[i] = i;
            list.add(i);
        }
        final Random rnd = new Random(System.nanoTime());

        tests.addTest("list", new Runnable() {

            @Override
            public void run() {
                int i = rnd.nextInt(SIZE);
                assertEquals(i, (int)list.get(i));
            }

        });

        tests.addTest("array", new Runnable() {

            @Override
            public void run() {
                int i = rnd.nextInt(SIZE);
                assertEquals(i, array[i]);
            }

        });
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
        assertion.assertTest("array").fasterThan("list");
    }

}
