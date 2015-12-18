package com.fillumina.lcs.recursive;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import java.util.List;
import org.junit.Ignore;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MemoizedRecursiveLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new MemoizedRecursiveLcs() {

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
