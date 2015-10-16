package com.fillumina.lcs.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BidirectionalVectorTest {

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
