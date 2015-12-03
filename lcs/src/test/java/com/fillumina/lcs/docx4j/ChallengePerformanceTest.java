package com.fillumina.lcs.docx4j;

import com.fillumina.lcs.AbstractLinearSpaceMyersLcIndex;
import com.fillumina.lcs.LcsItem;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import com.fillumina.performance.consumer.assertion.PerformanceAssertion;
import com.fillumina.performance.producer.TestContainer;
import com.fillumina.performance.template.AutoProgressionPerformanceTemplate;
import com.fillumina.performance.template.ProgressionConfigurator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ChallengePerformanceTest
        extends AutoProgressionPerformanceTemplate {

    private static final int TOTAL = 6000;
    private static final int LCS = 5000;
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
                ibmLcsLength.run();
                assertEquals(LCS, ibmLcsLength.getLcs());
            }
        });

        tests.addTest("mine", new Runnable() {
            @Override
            public void run() {
                final MyLcsLength myLcsLength =
                        new MyLcsLength(a, b);
                myLcsLength.run();
                assertEquals(LCS, myLcsLength.getLcs());
            }
        });
    }

    @Override
    public void addAssertions(PerformanceAssertion assertion) {
    }

    private static class IbmLcsLength extends LCS implements Runnable {
        private static final LCSSettings LCS_SETTINGS = new LCSSettings();
        private final Object[] a, b;
        private int lcs;

        public IbmLcsLength(List<?> a, List<?> b) {
            this.a = a.toArray(new Object[a.size()]);
            this.b = b.toArray(new Object[b.size()]);
        }

        public int getLcs() {
            return lcs;
        }

        @Override
        protected int getLength2() {
            return b.length;
        }

        @Override
        protected int getLength1() {
            return a.length;
        }

        @Override
        protected boolean isRangeEqual(int i1, int i2) {
            return Objects.equals(a[i1], b[i2]);
        }

        @Override
        protected void setLcs(int sl1, int sl2) {
            lcs++;
        }

        @Override
        protected void initializeLcs(int lcsLength) {
        }

        @Override
        public void run() {
            longestCommonSubsequence(LCS_SETTINGS);
        }
    }

    private static class MyLcsLength
            extends AbstractLinearSpaceMyersLcIndex
            implements Runnable {
        private final Object[] a, b;
        private int lcs;

        public MyLcsLength(List<?> a, List<?> b) {
            this.a = a.toArray(new Object[a.size()]);
            this.b = b.toArray(new Object[b.size()]);
        }

        public int getLcs() {
            return lcs;
        }

        @Override
        protected int getFirstSequenceLength() {
            return a.length;
        }

        @Override
        protected int getSecondSequenceLength() {
            return b.length;
        }

        @Override
        protected boolean equals(int x, int y) {
            return Objects.equals(a[x], b[y]);
        }

        @Override
        protected LcsItem match(int x, int y, int steps) {
            lcs += steps;
            return null;
        }

        @Override
        public void run() {
            calculateLcs();
        }
    }
}
