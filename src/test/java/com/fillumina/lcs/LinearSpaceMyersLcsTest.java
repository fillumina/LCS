package com.fillumina.lcs;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsTest {

    private final AbstractLcsTestExecutor executor = new AbstractLcsTestExecutor() {

        @Override
        protected Lcs<Character> getLcs() {
            return new LinearSpaceMyersLcs<>();
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
