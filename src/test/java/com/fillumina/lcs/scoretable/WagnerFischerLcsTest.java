package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.AbstractLcsTestExecutor;
import com.fillumina.lcs.ListLcs;
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
            protected ListLcs<?> getLcsAlgorithm() {
                return new WagnerFischerLcs<>();
            }

        }.lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN");
    }


}
