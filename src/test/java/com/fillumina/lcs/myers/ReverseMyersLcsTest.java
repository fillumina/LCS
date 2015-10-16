package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTestExecutor;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.util.BidirectionalArray;
import com.fillumina.lcs.util.BidirectionalVector;
import com.fillumina.lcs.util.OneBasedVector;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ReverseMyersLcsTest {

    private final AbstractLcsTestExecutor executor = new AbstractLcsTestExecutor() {

        @Override
        protected Lcs<Character> getLcs() {
            return new ReverseMyersLcs<>();
        }

    };

    @Test
    public void shouldWorkABCABBA() {

        executor.lcs("ABCABBA", "CBABAC")
                .assertResult("BABA"); // it's the reverse path

    }

    @Test
    public void shouldWorkPYTHON() {

        executor.lcs("PYTHON", "PONY")
                .assertResult("PON");

    }
}
