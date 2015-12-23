package com.fillumina.lcs.algorithm.recursive;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.testutil.Converter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class RecursiveLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new RecursiveLcs();
    }

    @Test
    public void shouldGetTheRightResult() {
        CountingRecursiveLcs lcs = new CountingRecursiveLcs();
        lcs.lcs(Converter.toArray("HUMAN"), Converter.toArray("CHIMPANZEE"));
        assertEquals(2221, lcs.getCounter());
    }

    private static class CountingRecursiveLcs extends RecursiveLcs {
        private int counter;

        public int getCounter() {
            return counter;
        }

        @Override
        <T> Stack<T> recursiveLcs(T[] a, int n, T[] b, int m) {
            counter++;
            return super.recursiveLcs(a, n, b, m);
        }
    }

    /**
     * Cannot use the standard test because this algorithm is VERY
     * memory hungry and time consuming.
     */
    @Test(timeout = 2_000L)
    @Override
    public void shouldPassLengthTest() {
        randomLcs(10, 5, 1);
    }

    @Test(timeout = 10_000L)
    @Override
    public void shouldPerformRandomLengthTests() {
        randomLcs(7, 4, 100);
    }
}
