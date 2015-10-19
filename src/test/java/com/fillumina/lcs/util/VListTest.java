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
    public void shouldReverseTheList() {
        VList<Integer> reversed = wrapper.reverse();
        assertList(reversed, 6, 5, 4, 3, 2, 1);
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
    public void shouldReverseASublistFromNonZero() {
        VList<Integer> sublist = wrapper.subList(2, 4);
        assertList(sublist, 2, 3);
        VList<Integer> reversed = sublist.reverse();
        assertList(reversed, 3, 2);
    }

    @Test
    public void shouldSublistAReversedSublist() {
        VList<Integer> sublist = wrapper.subList(2, 5);
        assertList(sublist, 2, 3, 4);

        VList<Integer> reversed = sublist.reverse();
        assertList(reversed, 4, 3, 2);

        VList<Integer> sublistedReverse = reversed.subList(2, 3);
        assertList(sublistedReverse, 3);
    }

    @Test
    public void shouldSubListASublistedReversedSublist() {
        VList<Integer> list1 = wrapper.subList(2, 6);
        assertList(list1, 2, 3, 4, 5);

        VList<Integer> list2 = list1.reverse();
        assertList(list2, 5, 4, 3, 2);

        VList<Integer> list3 = list2.subList(2, 4);
        assertList(list3, 4, 3);

        VList<Integer> list4 = list3.reverse();
        assertList(list4, 3, 4);

        VList<Integer> list5 = list4.subList(1, 2);
        assertList(list5, 3);
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
        assertEquals(2, sublist.zero());
    }

    @Test
    public void shouldGetAllOfAReversedSublist() {
        VList<Integer> sublist = wrapper.subList(2, 6);
        assertList(sublist, 2, 3, 4, 5);

        VList<Integer> reversed = sublist.reverse();
        assertList(reversed, 5, 4, 3, 2);

        VList<Integer> all = reversed.subList(1, 5);
        assertList(all, 5, 4, 3, 2);
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
    public void shouldSublistAllReversedList() {
        VList<Integer> reversed = wrapper.reverse();
        assertList(reversed, 6, 5, 4, 3, 2, 1);

        VList<Integer> sublist = reversed.subList(1, 7);
        assertList(sublist, 6, 5, 4, 3, 2, 1);
    }

    @Test
    public void shouldSublistAReversedList() {
        VList<Integer> reversed = wrapper.reverse();
        assertList(reversed, 6, 5, 4, 3, 2, 1);

        VList<Integer> sublist = reversed.subList(3, 6);
        assertList(sublist, 4, 3, 2);

        VList<Integer> sublist1 = reversed.subList(1, 3);
        assertList(sublist1, 6, 5);

        VList<Integer> sublist2 = reversed.subList(5, 7);
        assertList(sublist2, 2, 1);

        VList<Integer> sublist3 = reversed.subList(6, 7);
        assertList(sublist3, 1);
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
    public void shouldReverseASublist() {
        VList<Integer> sublist = wrapper.subList(1, 5);
        assertList(sublist, 1, 2, 3, 4);

        VList<Integer> reversed = sublist.reverse();
        assertList(reversed, 4, 3, 2, 1);
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
