package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTestExecutor;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.LinearSpaceMyersLcs0;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs0Test {

    private final AbstractLcsTestExecutor executor = new AbstractLcsTestExecutor() {

        @Override
        protected Lcs<Character> getLcs() {
            return new LinearSpaceMyersLcs0<>();
        }

    };

    @Test
    public void shouldWorkABCABBA() {

        executor.lcs("ABCABBA", "CBABAC")
                .assertResult("CABA");

    }

    @Test
    public void shouldWorkPYTHON() {

        executor.lcs("PYTHON", "PONY")
                .assertResult("PON");

    }
}
