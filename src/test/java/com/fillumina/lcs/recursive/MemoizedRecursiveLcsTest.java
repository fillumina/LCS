package com.fillumina.lcs.recursive;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import java.util.List;
import org.junit.Ignore;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MemoizedRecursiveLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new MemoizedRecursiveLcs() {

            @Override
            public <T> List<? extends T> lcs(List<? extends T> a, int n,
                    List<? extends T> b, int m) {
                count(a, b);
                return super.lcs(a, n, b, m);
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
