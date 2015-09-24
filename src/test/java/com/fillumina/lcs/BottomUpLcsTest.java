package com.fillumina.lcs;

import java.util.List;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BottomUpLcsTest {

    @Test
    public void shouldGetTheRightResult() {


        new LcsCountingTestExecutor() {

            @Override
            protected Lcs<Character> getLcs() {
                return new BottomUpLcs<>();
            }

        }.lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN");
    }
}
