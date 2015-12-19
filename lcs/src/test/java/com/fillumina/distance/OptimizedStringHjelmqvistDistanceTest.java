package com.fillumina.distance;

import com.fillumina.distance.OptimizedStringHjelmqvistDistance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedStringHjelmqvistDistanceTest {

    @Test
    public void testCloseDistance() {
        assertEquals(2, OptimizedStringHjelmqvistDistance.distance("tuesday", "thursday"));
    }

    @Test
    public void testFarDistance() {
        assertEquals(5, OptimizedStringHjelmqvistDistance.distance("monday", "saturday"));
    }

    @Test
    public void testSameString() {
        assertEquals(0, OptimizedStringHjelmqvistDistance.distance("sunday", "sunday"));
    }

    @Test
    public void testAlmostSameString() {
        assertEquals(1, OptimizedStringHjelmqvistDistance.distance("sunday", "sunday2"));
    }
}
