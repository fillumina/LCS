package com.fillumina.lcs.algorithm.myers.linearspace;

import com.fillumina.lcs.algorithm.myers.linearspace.RLinearSpaceMyersLcs;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class RLinearSpaceMyersLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new RLinearSpaceMyersLcs();
    }

    public static void main(String[] args) {
        new RLinearSpaceMyersLcsTest().randomLcs(60, 10, 100);
    }

    @Test
    public void shouldGetHUM() {
        lcs("HUM", "CHIM").assertResult("HM");
    }
}
