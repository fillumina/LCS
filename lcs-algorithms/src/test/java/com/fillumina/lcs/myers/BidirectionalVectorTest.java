package com.fillumina.lcs.myers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati 
 */
public class BidirectionalVectorTest {

    @Test
    public void shouldCreateAnEmptyVector() {
        BidirectionalVector v = new BidirectionalVector(0);
        v.set(0, 1);
        assertEquals(1, v.get(0));
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
