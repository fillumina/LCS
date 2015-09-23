package com.fillumina.lcs;

import java.util.List;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MemoizedRecursiveLcsTest {

    @Test
    public void shouldGetTheRightResult() {

        new LcsCountingTestExecutor() {

            @Override
            protected Lcs<Character> getLcs() {
                return new MemoizedRecursiveLcs<Character>() {

                    @Override
                    public List<Character> lcs(List<Character> xs,
                            List<Character> ys) {
                        count(xs, ys);
                        return super.lcs(xs, ys);
                    }
                };
            }

        }.lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN")
                .assertNumberOfCalls(69)
                .printerr();
    }
}
