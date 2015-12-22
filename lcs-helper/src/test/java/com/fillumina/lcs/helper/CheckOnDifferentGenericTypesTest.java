package com.fillumina.lcs.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CheckOnDifferentGenericTypesTest {

    private <T> int typed(T[] a, T[] b) {
        return a.length + b.length;
    }

    private int untyped(Object[] a, Object[] b) {
        return a.length + b.length;
    }

    @Test
    public void shouldWorkOnDifferentTypes() {
        assertEquals(20, typed(new Integer[10], new String[10]));
        assertEquals(20, untyped(new Integer[10], new String[10]));
    }

    private <T> List<T> toList(T[] a, T[] b) {
        List<T> list = new ArrayList<>();
        list.addAll(Arrays.asList(a));
        list.addAll(Arrays.asList(b));
        return list;
    }

    @Test
    public void shouldWorkWithCharacter() {
        List<Character> list = toList(new Character[]{'a'},
                new Character[]{'b', 'c'});
        assertEquals(new Character('b'), list.get(1));
        assertEquals(3, list.size());
    }

    // DON'T COMPILE
//    @Test
//    public void shouldWorkWithMixed() {
//        List<Number> list = toList(new Short[]{12},
//                new Integer[]{3, 5});
//        assertEquals(3, list.size());
//    }

}
