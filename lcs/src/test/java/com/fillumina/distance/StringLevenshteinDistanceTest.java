package com.fillumina.distance;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class StringLevenshteinDistanceTest {

    @Test
    public void testCloseDistance() {
        assertEquals(2, CharLevenshteinDistance.distance("tuesday", "thursday"));
    }

    @Test
    public void testFarDistance() {
        assertEquals(5, CharLevenshteinDistance.distance("monday", "saturday"));
    }
}
