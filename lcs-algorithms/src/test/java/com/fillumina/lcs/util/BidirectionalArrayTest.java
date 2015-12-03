package com.fillumina.lcs.util;

import com.fillumina.lcs.util.BidirectionalArray;
import com.fillumina.lcs.util.BidirectionalVector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BidirectionalArrayTest {

    @Test
    public void shouldGetBidirectionalArray() {
        BidirectionalArray a = new BidirectionalArray(3);

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
        BidirectionalArray a = new BidirectionalArray(3);
        BidirectionalVector v = new BidirectionalVector(3);

        for (int i = -3; i <= 3; i++) {
            v.set(i, i);
        }

        a.copyVectorOnLine(0, v);
        for (int i = -3; i <= 3; i++) {
            assertEquals(i, a.get(0, i));
        }

        a.copyVectorOnLine(1, v);
        for (int i = -3; i <= 3; i++) {
            assertEquals(i, a.get(1, i));
        }
    }

}
