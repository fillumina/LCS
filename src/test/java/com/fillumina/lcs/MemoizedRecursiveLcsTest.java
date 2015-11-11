package com.fillumina.lcs;

import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MemoizedRecursiveLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new MemoizedRecursiveLcs<Character>() {

            @Override
            public List<Character> lcs(List<Character> xs, int n,
                    List<Character> ys, int m) {
                count(xs, ys);
                return super.lcs(xs, n, ys, m);
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
