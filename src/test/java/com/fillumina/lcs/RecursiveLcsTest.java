package com.fillumina.lcs;

import java.util.List;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RecursiveLcsTest {

    @Test
    public void shouldGetTheRightResult() {


        new AbstractLcsTestExecutor() {

            @Override
            protected Lcs<Character> getLcs() {
                return new RecursiveLcs<Character>() {

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
                .assertNumberOfCalls(2221);
    }
}
