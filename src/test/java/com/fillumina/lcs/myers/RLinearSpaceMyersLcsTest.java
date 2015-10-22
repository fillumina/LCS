package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.Lcs;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RLinearSpaceMyersLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new RLinearSpaceMyersLcs<>();
    }

    @Test
    public void shouldGetHUM() {
        lcs("HUM", "CHIM").assertResult("HM");
    }
}
