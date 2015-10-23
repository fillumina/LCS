package com.fillumina.lcs.util;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class VListTest {

    VList<Integer> wrapper;

    public VListTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        wrapper = new VList<>(list);
    }

    @Test
    public void shouldWrapAList() {
        assertList(wrapper, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void shouldSublistFromOne() {
        VList<Integer> sublist = wrapper.subList(1, 3);
        assertList(sublist, 1, 2);
    }

    @Test
    public void shouldGetTheSizeOfASubList() {
        VList<Integer> sublist = wrapper.subList(2, 4);
        assertEquals(2, sublist.size());
    }

    @Test
    public void shouldSublistFromNonOne() {
        VList<Integer> sublist = wrapper.subList(2, 4);
        assertList(sublist, 2, 3);
    }

    @Test
    public void shouldSublist() {
        VList<Integer> sublist = wrapper.subList(2, 6);
        assertList(sublist, 2, 3, 4, 5);
    }

    @Test
    public void shouldGetAllOfASublist() {
        VList<Integer> sublist = wrapper.subList(2, 6);
        assertList(sublist, 2, 3, 4, 5);

        VList<Integer> all = sublist.subList(1, 5);
        assertList(all, 2, 3, 4, 5);
    }

    @Test
    public void shouldGetZeroOfASublist() {
        VList<Integer> sublist = wrapper.subList(2, 6);
        assertEquals(1, sublist.zero());
    }

    @Test
    public void shouldSublistIncludingStart() {
        VList<Integer> sublist = wrapper.subList(1, 4);
        assertList(sublist, 1, 2, 3);
    }

    @Test
    public void shouldSublistIncludingEnd() {
        VList<Integer> sublist = wrapper.subList(5, 7);
        assertList(sublist, 5, 6);
    }

    @Test
    public void shouldSublistAllList() {
        VList<Integer> sublist = wrapper.subList(1, 7);
        assertList(sublist, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void shouldSublistOfZeroSizeBeEmpty() {
        VList<Integer> empty = wrapper.subList(3, 3);
        assertEquals(0, empty.size());
    }

    @Test
    public void shouldSublistOfInvertedIdexesBeEmpty() {
        VList<Integer> empty = wrapper.subList(5, 3);
        assertEquals(0, empty.size());
    }

    @Test
    public void shouldReturnTheCorrectSize() {
        assertEquals(3, wrapper.subList(1, 4).size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldTestUpperBoundaries() {
        wrapper.subList(2,4).get(3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldTestLowerBoundaries() {
        wrapper.subList(1, 1).get(1);
    }

    private <T> void assertList(VList<T> list, T... values) {
        assertEquals("size mismatch", values.length, list.size());
        for (int i=0; i<values.length; i++) {
            assertEquals("index=" + i, values[i], list.get(i+1));
        }
    }
}
