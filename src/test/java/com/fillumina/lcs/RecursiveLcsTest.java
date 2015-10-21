package com.fillumina.lcs;

import java.util.List;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RecursiveLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new RecursiveLcs<Character>() {

            @Override
            public List<Character> lcs(List<Character> xs,
                    List<Character> ys) {
                count(xs, ys);
                return super.lcs(xs, ys);
            }
        };
    }

    @Test
    public void shouldGetTheRightResult() {
        lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN")
                .assertNumberOfCalls(2221);
    }
}
