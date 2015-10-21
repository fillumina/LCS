package com.fillumina.lcs;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WagnerFischerLcsTest {

    @Test
    public void shouldReturnTheRightResult() {
        new AbstractLcsTestExecutor() {

            @Override
            protected Lcs<Character> getLcsAlgorithm() {
                return new WagnerFischerLcs<>();
            }

        }.lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN");
    }


}
