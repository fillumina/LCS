package com.fillumina.lcs.util;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OneBasedVectorTest {

    @Test
    public void shouldGetOneBasedVector() {
        OneBasedVector<Character> v = new OneBasedVector<>(
                Arrays.asList('H', 'E', 'L', 'O'));

        assertEquals(Character.valueOf('H'), v.get(1));
        assertEquals(Character.valueOf('E'), v.get(2));
        assertEquals(Character.valueOf('L'), v.get(3));
        assertEquals(Character.valueOf('O'), v.get(4));
    }
}
