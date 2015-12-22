package com.fillumina.lcs.testutil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class ConversionHelperTest {

    private static final String STRING = "hello world";
    private static final List<Character> LIST = Arrays.asList(
            'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd');

    @Test
    public void shouldTransformToList() {
        assertEquals(LIST, ConversionHelper.toList(STRING));
    }

    @Test
    public void shouldTrasformAnEmptyString() {
        assertEquals(Collections.<Character>emptyList(),
                ConversionHelper.toList(""));
    }

    @Test
    public void shouldTrasformToASingletonString() {
        assertEquals(Collections.<Character>singletonList('A'),
                ConversionHelper.toList("A"));
    }

    @Test
    public void shouldTransformToString() {
        assertEquals(STRING, ConversionHelper.toString(LIST));
    }

    @Test
    public void shouldTrasformIntoAnEmptyString() {
        assertEquals("", ConversionHelper.toString(
                Collections.<Character>emptyList()));
    }

    @Test
    public void shouldTrasformIntoASingletonString() {
        assertEquals("Z", ConversionHelper.toString(
                Collections.<Character>singletonList('Z')));
    }
}
