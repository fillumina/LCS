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
    protected Lcs<?> getLcsAlgorithm() {
        return new RLinearSpaceMyersLcs<>();
    }

    public static void main(String[] args) {
        System.out.println("testing random sequences...");
        final RLinearSpaceMyersLcsTest test = new RLinearSpaceMyersLcsTest();
        for (int i=0; i<100; i++) {
            System.out.println("iteration: " + i);
            test.shouldPassLengthTest();
        }
    }

    @Test
    public void shouldGetHUM() {
        lcs("HUM", "CHIM").assertResult("HM");
    }
}
