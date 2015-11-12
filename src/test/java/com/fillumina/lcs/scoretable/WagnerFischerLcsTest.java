package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.AbstractLcsTestExecutor;
import com.fillumina.lcs.ListLcs;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WagnerFischerLcsTest {

    @Test
    public void shouldReturnTheRightResult() {
        new AbstractLcsTestExecutor() {

            @Override
            protected ListLcs<?> getLcsAlgorithm() {
                return new WagnerFischerLcs<>();
            }

        }.lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN");
    }


}
