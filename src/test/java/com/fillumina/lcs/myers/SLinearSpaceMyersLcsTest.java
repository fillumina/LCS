package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTestExecutor;
import com.fillumina.lcs.Lcs;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SLinearSpaceMyersLcsTest {

    private final AbstractLcsTestExecutor executor = new AbstractLcsTestExecutor() {

        @Override
        protected Lcs<Character> getLcs() {
            return new SLinearSpaceMyersLcs<>();
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
