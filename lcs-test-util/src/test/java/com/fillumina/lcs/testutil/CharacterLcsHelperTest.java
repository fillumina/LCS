package com.fillumina.lcs.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
public class CharacterLcsHelperTest {

    private static final String STRING = "hello world";
    private static final List<Character> LIST = Arrays.asList(
            'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd');

    @Test
    public void shouldTransformToList() {
        assertEquals(LIST, CharacterLcsHelper.toList(STRING));
    }

    @Test
    public void shouldTrasformAnEmptyString() {
        assertEquals(Collections.<Character>emptyList(),
                CharacterLcsHelper.toList(""));
    }

    @Test
    public void shouldTrasformToASingletonString() {
        assertEquals(Collections.<Character>singletonList('A'),
                CharacterLcsHelper.toList("A"));
    }

    @Test
    public void shouldTransformToString() {
        assertEquals(STRING, CharacterLcsHelper.toString(LIST));
    }

    @Test
    public void shouldTrasformIntoAnEmptyString() {
        assertEquals("", CharacterLcsHelper.toString(
                Collections.<Character>emptyList()));
    }

    @Test
    public void shouldTrasformIntoASingletonString() {
        assertEquals("Z", CharacterLcsHelper.toString(
                Collections.<Character>singletonList('Z')));
    }
}
