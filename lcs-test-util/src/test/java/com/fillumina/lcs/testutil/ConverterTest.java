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
public class ConverterTest {

    private static final String STRING = "hello world";
    private static final List<Character> LIST = Arrays.asList(
            'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd');

    @Test
    public void shouldTransformToList() {
        assertEquals(LIST, Converter.toList(STRING));
    }

    @Test
    public void shouldTrasformAnEmptyString() {
        assertEquals(Collections.<Character>emptyList(),
                Converter.toList(""));
    }

    @Test
    public void shouldTrasformToASingletonString() {
        assertEquals(Collections.<Character>singletonList('A'),
                Converter.toList("A"));
    }

    @Test
    public void shouldTransformToString() {
        assertEquals(STRING, Converter.toString(LIST));
    }

    @Test
    public void shouldTrasformIntoAnEmptyString() {
        assertEquals("", Converter.toString(
                Collections.<Character>emptyList()));
    }

    @Test
    public void shouldTrasformIntoASingletonString() {
        assertEquals("Z", Converter.toString(
                Collections.<Character>singletonList('Z')));
    }
}
