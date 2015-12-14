package com.fillumina.lcs.myers;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class VListTest {

    VList<Integer> vlist;

    public VListTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        vlist = new VList<>(list);
    }

    @Test
    public void shouldWrapAList() {
        assertList(vlist, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void shouldSublistFromOne() {
        VList<Integer> sublist = vlist.subList(1, 3);
        assertList(sublist, 2, 3);
    }

    @Test
    public void shouldGetTheSizeOfASubList() {
        VList<Integer> sublist = vlist.subList(2, 4);
        assertEquals(2, sublist.size());
    }

    @Test
    public void shouldSublistFromNonOne() {
        VList<Integer> sublist = vlist.subList(2, 4);
        assertList(sublist, 3, 4);
    }

    @Test
    public void shouldSublist() {
        VList<Integer> sublist = vlist.subList(2, 6);
        assertList(sublist, 3, 4, 5, 6);
    }

    @Test
    public void shouldGetFromSublist() {
        VList<Integer> sublist = vlist.subList(2, 6);
        assertEquals(3, sublist.get(0), 0);
        assertEquals(6, sublist.get(3), 0);
    }

    @Test
    public void shouldGetAllOfASublist() {
        VList<Integer> sublist = vlist.subList(2, 6);
        assertList(sublist, 3, 4, 5, 6);

        VList<Integer> all = sublist.subList(1, 3);
        assertList(all, 4, 5);
    }

    @Test
    public void shouldGetZeroOfASublist() {
        VList<Integer> sublist = vlist.subList(1, 6);
        assertEquals(1, sublist.zero());
    }

    @Test
    public void shouldSublistIncludingStart() {
        VList<Integer> sublist = vlist.subList(0, 4);
        assertList(sublist, 1, 2, 3, 4);
    }

    @Test
    public void shouldSublistIncludingEnd() {
        VList<Integer> sublist = vlist.subList(4, 6);
        assertList(sublist, 5, 6);
    }

    @Test
    public void shouldSublistAllList() {
        VList<Integer> sublist = vlist.subList(0, 6);
        assertList(sublist, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void shouldSublistOfZeroSizeBeEmpty() {
        VList<Integer> empty = vlist.subList(3, 3);
        assertEquals(0, empty.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldSublistOfInvertedIdexesThrowException() {
        VList<Integer> empty = vlist.subList(5, 3);
        assertEquals(0, empty.size());
    }

    @Test
    public void shouldReturnTheCorrectSize() {
        assertEquals(3, vlist.subList(1, 4).size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldTestUpperBoundaries() {
        vlist.subList(2,4).get(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldTestLowerBoundaries() {
        vlist.subList(1, 1).get(0);
    }

    private <T> void assertList(VList<T> list, T... values) {
        assertEquals("size mismatch", values.length, list.size());
        for (int i=0; i<values.length; i++) {
            assertEquals("index=" + i, values[i], list.get(i));
        }
    }
}
