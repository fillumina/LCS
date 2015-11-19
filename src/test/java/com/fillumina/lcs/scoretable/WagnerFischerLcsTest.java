package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.AbstractLcsTestExecutor;
import org.junit.Test;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WagnerFischerLcsTest {

    @Test
    public void shouldReturnTheRightResult() {
        new AbstractLcsTestExecutor() {

            @Override
            public Lcs getLcsAlgorithm() {
                return new WagnerFischerLcs();
            }
        };
    }


}
