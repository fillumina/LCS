package com.fillumina.lcs.algorithm.recursive;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.testutil.Converter;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class MemoizedRecursiveLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new MemoizedRecursiveLcs();
    }

    private static class CountingMemoizedRecursiveLcs implements LcsList {
        private int counter;

        public int getCounter() {
            return counter;
        }

        @Override
        public <T> List<T> lcs(T[] a, T[] b) {
            return new CountingInner().lcs(a, b);
        }

        private class CountingInner extends MemoizedRecursiveLcs.Inner {

            @Override
            <T> Stack<T> recursiveLcs(T[] a, int n, T[] b, int m) {
                counter++;
                return super.recursiveLcs(a, n, b, m);
            }
        }
    }

    @Test
    public void shouldGetLowerCounterThanDirect() {
        CountingMemoizedRecursiveLcs lcs = new CountingMemoizedRecursiveLcs();
        lcs.lcs(Converter.toArray("HUMAN"), Converter.toArray("CHIMPANZEE"));
        assertEquals(69, lcs.getCounter());
    }
}
