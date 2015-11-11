package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTestExecutor;
import com.fillumina.lcs.myers.SpaceOptimizedMyersLcs.BidirectionalVector;
import com.fillumina.lcs.myers.SpaceOptimizedMyersLcs.EndPointTable;
import com.fillumina.lcs.myers.SpaceOptimizedMyersLcs.OneBasedVector;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fillumina.lcs.ListLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SpaceOptimizedMyersLcsTest {

    private final AbstractLcsTestExecutor executor = new AbstractLcsTestExecutor() {

        @Override
        protected ListLcs<?> getLcsAlgorithm() {
            return new SpaceOptimizedMyersLcs<>();
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

    @Test
    public void shouldGetEndPOintTable() {
        EndPointTable a = new EndPointTable(4);

        for (int d = 0; d < 4; d++) {
            for (int k = -d; k <= d; k+=2) {
                a.set(d, k, k*d);
            }
        }

        for (int d = 0; d < 4; d++) {
            for (int k = -d; k <= d; k+=2) {
                assertEquals(k*d, a.get(d, k));
            }
        }
    }

    @Test
    public void shouldCopyABidirectionalVector() {
        EndPointTable a = new EndPointTable(4);
        BidirectionalVector v = new BidirectionalVector(3);

        for (int i = -3; i <= 3; i++) {
            v.set(i, i);
        }

        a.copy(3, v);
        for (int i = -3; i <= 3; i+=2) {
            assertEquals(i, a.get(3, i));
        }
    }
}
