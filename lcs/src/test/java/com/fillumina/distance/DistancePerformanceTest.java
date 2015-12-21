package com.fillumina.distance;

import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class DistancePerformanceTest extends AutoProgressionPerformanceTemplate {

    public static void main(String[] args) {
        System.out.println("performance evaluation, please wait...");
        new DistancePerformanceTest().executeWithIntermediateOutput();
    }

    private final String a, b;
    private final int distanceAB;

    public DistancePerformanceTest() {
        Locale[] la = Locale.getAvailableLocales();
        Locale[] lb = Locale.getAvailableLocales();
        shuffle(la);
        shuffle(lb);
        a = createString(la);
        b = createString(lb);
        distanceAB = StringLevenshteinDistance.distance(a, b);
        System.out.println("length=" + a.length() + ", distance=" + distanceAB);
    }

    private void shuffle(Locale[] l) {
        Random rnd = ThreadLocalRandom.current();
        Locale tmp;
        for (int i=0; i<10; i++) {
            int i1 = rnd.nextInt(l.length);
            int i2 = rnd.nextInt(l.length);
            tmp = l[i1];
            l[i1] = l[i2];
            l[i2] = tmp;
        }
    }

    private String createString(Locale[] locales) {
        StringBuilder buf = new StringBuilder();
        for (Locale l : locales) {
            buf.append(l.toString());
        }
        return buf.toString();
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
                assertEquals(distanceAB,
                        StringLevenshteinDistance.distance(a, b));
            }
        });

        tests.addTest("CharDistance", new Runnable() {
            @Override
            public void run() {
                assertEquals(2, CharLevenshteinDistance
                        .distance("tuesday", "thursday"));
                assertEquals(5, CharLevenshteinDistance
                        .distance("monday", "saturday"));
                assertEquals(distanceAB,
                        CharLevenshteinDistance.distance(a, b));
            }
        });

        tests.addTest("StringHjelmqvistDistance", new Runnable() {
            @Override
            public void run() {
                assertEquals(2, StringHjelmqvistLevenshteinDistance
                        .distance("tuesday", "thursday"));
                assertEquals(5, StringHjelmqvistLevenshteinDistance
                        .distance("monday", "saturday"));
                assertEquals(distanceAB,
                        StringHjelmqvistLevenshteinDistance.distance(a, b));
            }
        });

        tests.addTest("OptimiziedStringHjelmqvistDistance", new Runnable() {
            @Override
            public void run() {
                assertEquals(2, OptimizedStringHjelmqvistDistance
                        .distance("tuesday", "thursday"));
                assertEquals(5, StringHjelmqvistLevenshteinDistance
                        .distance("monday", "saturday"));
                assertEquals(distanceAB,
                        OptimizedStringHjelmqvistDistance.distance(a, b));
            }
        });
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }

}
