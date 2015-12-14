package com.fillumina.lcs.myers.linearspace;

import com.fillumina.lcs.myers.linearspace.RLinearSpaceMyersLcs;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import org.junit.Test;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RLinearSpaceMyersLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
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
