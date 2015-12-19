package com.fillumina.distance;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class StringHjelmqvistDistanceTest {

    @Test
    public void testCloseDistance() {
        assertEquals(2, StringHjelmqvistLevenshteinDistance.distance("tuesday", "thursday"));
    }

    @Test
    public void testFarDistance() {
        assertEquals(5, StringHjelmqvistLevenshteinDistance.distance("monday", "saturday"));
    }

    @Test
    public void testSameString() {
        assertEquals(0, StringHjelmqvistLevenshteinDistance.distance("sunday", "sunday"));
    }

    @Test
    public void testAlmostSameString() {
        assertEquals(1, StringHjelmqvistLevenshteinDistance.distance("sunday", "sunday2"));
    }
}
