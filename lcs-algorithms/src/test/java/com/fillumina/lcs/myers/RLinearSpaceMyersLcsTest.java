package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import org.junit.Test;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.RLinearSpaceMyersLcs;

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