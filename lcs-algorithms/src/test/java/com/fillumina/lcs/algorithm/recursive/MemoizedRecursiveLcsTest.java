package com.fillumina.lcs.algorithm.recursive;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import java.util.List;
import org.junit.Ignore;

/**
 *
 * @author Francesco Illuminati
 */
public class MemoizedRecursiveLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new MemoizedRecursiveLcs.Inner() {

            @Override
            <T> Stack<T> recursiveLcs(List<? extends T> a, int n,
                    List<? extends T> b, int m) {
                count(a, b);
                return super.recursiveLcs(a, n, b, m);
            }
        };
    }

    @Ignore
    public void shouldGetLowerCounterThanDirect() {
        lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN")
                .assertNumberOfCalls(69)
                .printerr();
    }
}