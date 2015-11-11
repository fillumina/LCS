package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import org.junit.Test;
import com.fillumina.lcs.ListLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RLinearSpaceMyersLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new RLinearSpaceMyersLcs<>();
    }

    public static void main(String[] args) {
        new RLinearSpaceMyersLcsTest().randomLcs(60, 10);
    }

    @Test
    public void shouldGetHUM() {
        lcs("HUM", "CHIM").assertResult("HM");
    }
}
