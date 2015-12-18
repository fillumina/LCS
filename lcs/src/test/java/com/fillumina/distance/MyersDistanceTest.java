package com.fillumina.distance;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersDistanceTest {

    @Test
    public void checkDistance() {
        assertEquals(3, MyersDistance
                .distance("tuesday", "thursday"));
        assertEquals(8, MyersDistance
                .distance("monday", "saturday"));
    }

}
