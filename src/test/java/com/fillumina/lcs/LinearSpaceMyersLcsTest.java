package com.fillumina.lcs;

import com.fillumina.lcs.LinearSpaceMyersLcs.BidirectionalVector;
import com.fillumina.lcs.LinearSpaceMyersLcs.OneBasedVector;
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

    @Test
    public void shouldGetOneBasedVector() {
        OneBasedVector<Character> v = new OneBasedVector<>(
                Arrays.asList('H', 'E', 'L', 'O'));

        assertEquals(Character.valueOf('H'), v.get(1));
        assertEquals(Character.valueOf('E'), v.get(2));
        assertEquals(Character.valueOf('L'), v.get(3));
        assertEquals(Character.valueOf('O'), v.get(4));
    }

    @Test
    public void shouldGetBidirectionalVector() {
        BidirectionalVector v = new BidirectionalVector(3);

        for (int i = -3; i <= 3; i++) {
            v.set(i, i);
        }
        for (int i = -3; i <= 3; i++) {
            assertEquals(i, v.get(i));
        }
    }

}
