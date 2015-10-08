package com.fillumina.lcs;

import com.fillumina.lcs.RListHirschbergLinearSpaceAlgorithmLcs.RList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RListTest {

    RList<Integer> wrapper;

    public RListTest() {
        List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5);
        wrapper = new RList<>(list);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotAdd() {
        wrapper.add(6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotRemove() {
        wrapper.remove(2);
    }

    @Test
    public void shouldWrapAList() {
        for (int i=0; i<6; i++) {
            assertEquals(i, (long)wrapper.get(i));
        }
    }

    @Test
    public void shouldReverseTheList() {
        RList<Integer> reversed = wrapper.reverse();
        for (int i=0; i<6; i++) {
            assertEquals(5 - i, (long)reversed.get(i));
        }
    }

    @Test
    public void shouldSublistFromZero() {
        RList<Integer> sublist = wrapper.subList(0, 3);
        for (int i=0; i<3; i++) {
            assertEquals(i, (long)sublist.get(i));
        }
    }

    @Test
    public void shouldSublistFromNonZero() {
        RList<Integer> sublist = wrapper.subList(2, 4);
        for (int i=0; i<2; i++) {
            assertEquals(i + 2, (long)sublist.get(i));
        }
    }

    @Test
    public void shouldReverseASublistFromNonZero() {
        RList<Integer> sublist = wrapper.subList(2, 4);
        RList<Integer> reversed = sublist.reverse();
        for (int i=0; i<2; i++) {
            assertEquals(3 - i, (long)reversed.get(i));
        }
    }

    @Test
    public void shouldSublistAReversedSublist() {
        RList<Integer> sublist = wrapper.subList(1, 4);
        RList<Integer> reversed = sublist.reverse();
        RList<Integer> sublistedReverse = reversed.subList(1, 2);
        assertEquals(2, (long)sublistedReverse.get(0));
    }

    @Test
    public void shouldReverseIterator() {
        RList<Integer> reversed = wrapper.reverse();
        int i = reversed.size() - 1;
        for (int r : reversed) {
            assertEquals(i, r);
            i--;
        }
    }

    @Test
    public void shouldReverseSublistedIterator() {
        RList<Integer> sublist = wrapper.subList(1, 4);
        RList<Integer> reversed = sublist.reverse();
        int i = 3;
        for (int r : reversed) {
            assertEquals(i, r);
            i--;
        }
    }

    @Test
    public void shouldReturnTheCorrectSize() {
        assertEquals(3, wrapper.subList(1, 4).size());
    }
}
