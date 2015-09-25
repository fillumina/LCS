package com.fillumina.lcs;

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

    @Test
    public void shouldEmptyListBeEmpty() {
        assertTrue(RList.emptyList().isEmpty());
    }

    @Test
    public void shouldEmptyListBeZeroSized() {
        assertEquals(0, RList.emptyList().size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldNotGetFromAnEmptyList() {
        RList.emptyList().get(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotAddElementToEmptyList() {
        RList.<String>emptyList().add("Hello");
    }

    @Test
    public void shouldAddListToEmptyList() {
        RList<String> l = new RList<>(Arrays.asList("one", "two", "three"));
        assertEquals(l, RList.<String>emptyList().add(l));
    }

    @Test
    public void shouldAddEmptyListToEmptyList() {
        RList<String> l = RList.<String>emptyList();
        assertEquals(l, RList.<String>emptyList().add(l));
    }

    @Test
    public void shouldReturnTheSingletonElement() {
        RList<String> singleton = RList.<String>singleton("hello");
        assertEquals("hello", singleton.get(0));
    }

    @Test
    public void shouldSingletonHasSizeOne() {
        assertEquals(1, RList.singleton("one").size());
    }

    @Test
    public void shouldSingletonBeNotEmpty() {
        assertFalse(RList.singleton("one").isEmpty());
    }

    @Test
    public void shouldAddRListToSingleton() {
        assertEquals(Arrays.asList("one", "two", "three"),
                RList.singleton("one").add(
                    RList.singleton("two").add(
                        RList.singleton("three"))));
    }
}
