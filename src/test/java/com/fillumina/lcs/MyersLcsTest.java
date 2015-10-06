package com.fillumina.lcs;

import com.fillumina.lcs.MyersLcs.BidirectionalArray;
import com.fillumina.lcs.MyersLcs.BidirectionalVector;
import com.fillumina.lcs.MyersLcs.OneBasedVector;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcsTest {

    private final AbstractLcsTestExecutor executor = new AbstractLcsTestExecutor() {

        @Override
        protected Lcs<Character> getLcs() {
            return new MyersLcs<>();
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
        BidirectionalVector v = new MyersLcs.BidirectionalVector(3);

        for (int i = -3; i <= 3; i++) {
            v.set(i, i);
        }
        for (int i = -3; i <= 3; i++) {
            assertEquals(i, v.get(i));
        }
    }

    @Test
    public void shouldGetBidirectionalArray() {
        BidirectionalArray a = new MyersLcs.BidirectionalArray(3);

        for (int d = 0; d < 3; d++) {
            for (int i = -3; i <= 3; i++) {
                a.set(d, i, i * d);
            }
        }

        for (int d = 0; d < 3; d++) {
            for (int i = -3; i <= 3; i++) {
                assertEquals(i*d, a.get(d, i));
            }
        }
    }

    @Test
    public void shouldCopyABidirectionalVector() {
        BidirectionalArray a = new MyersLcs.BidirectionalArray(3);
        BidirectionalVector v = new MyersLcs.BidirectionalVector(3);

        for (int i = -3; i <= 3; i++) {
            v.set(i, i);
        }

        a.copy(0, v);
        for (int i = -3; i <= 3; i++) {
            assertEquals(i, a.get(0, i));
        }

        a.copy(1, v);
        for (int i = -3; i <= 3; i++) {
            assertEquals(i, a.get(1, i));
        }
    }
}
