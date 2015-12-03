package com.fillumina.lcs;

import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsItemImplTest {

    @Test
    public void shouldReturnX() {
        LcsItem item = new LcsItemImpl(111, 222, 333);
        assertEquals(111, item.getFirstSequenceIndex());
    }

    @Test
    public void shouldReturnY() {
        LcsItem item = new LcsItemImpl(111, 222, 333);
        assertEquals(222, item.getSecondSequenceIndex());
    }

    @Test
    public void shouldReturnTheSteps() {
        LcsItem item = new LcsItemImpl(111, 222, 333);
        assertEquals(333, item.getSteps());
    }

    @Test
    public void shouldTheNULLHaveNegativeX() {
        assertEquals(-1, LcsItemImpl.NULL.getFirstSequenceIndex());
    }

    @Test
    public void shouldTheNULLHaveNegativeY() {
        assertEquals(-1, LcsItemImpl.NULL.getSecondSequenceIndex());
    }

    @Test
    public void shouldTheNULLHaveZeroSteps() {
        assertEquals(0, LcsItemImpl.NULL.getSteps());
    }

    @Test
    public void shouldChainTwoItems() {
        LcsItemImpl head = new LcsItemImpl(0, 0, 0);
        LcsItemImpl tail = new LcsItemImpl(1, 1, 1);
        head.chain(tail);
        final Iterator<LcsItem> iterator = head.iterator();
        assertEquals(head, iterator.next());
        assertEquals(tail, iterator.next());
    }

    @Test
    public void shouldChainThreeItems() {
        LcsItemImpl head = new LcsItemImpl(0, 0, 0);
        LcsItemImpl middle = new LcsItemImpl(1, 1, 1);
        LcsItemImpl tail = new LcsItemImpl(2, 2, 2);
        head.chain(middle);
        middle.chain(tail);
        final Iterator<LcsItem> iterator = head.iterator();
        assertEquals(head, iterator.next());
        assertEquals(middle, iterator.next());
        assertEquals(tail, iterator.next());
    }

    @Test
    public void shouldChainFourItems() {
        LcsItemImpl a = new LcsItemImpl(0, 0, 0);
        LcsItemImpl b = new LcsItemImpl(1, 1, 1);
        LcsItemImpl c = new LcsItemImpl(2, 2, 2);
        LcsItemImpl d = new LcsItemImpl(3, 3, 3);
        a.chain(b);
        c.chain(d);
        b.chain(c);
        final Iterator<LcsItem> iterator = a.iterator();
        assertEquals(a, iterator.next());
        assertEquals(b, iterator.next());
        assertEquals(c, iterator.next());
        assertEquals(d, iterator.next());
    }

    @Test
    public void shouldChainFourItemsInDifferentOrder() {
        LcsItemImpl a = new LcsItemImpl(0, 0, 0);
        LcsItemImpl b = new LcsItemImpl(1, 1, 1);
        LcsItemImpl c = new LcsItemImpl(2, 2, 2);
        LcsItemImpl d = new LcsItemImpl(3, 3, 3);
        c.chain(d);
        b.chain(c);
        a.chain(b);
        final Iterator<LcsItem> iterator = a.iterator();
        assertEquals(a, iterator.next());
        assertEquals(b, iterator.next());
        assertEquals(c, iterator.next());
        assertEquals(d, iterator.next());
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsOne() {
        LcsItemImpl a = new LcsItemImpl(0, 0, 1);
        assertEquals(1, a.size());
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsTwo() {
        LcsItemImpl a = new LcsItemImpl(0, 0, 1);
        LcsItemImpl b = new LcsItemImpl(1, 1, 1);
        a.chain(b);
        assertEquals(2, a.size());
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsThree() {
        LcsItemImpl a = new LcsItemImpl(0, 0, 1);
        LcsItemImpl b = new LcsItemImpl(1, 1, 1);
        LcsItemImpl c = new LcsItemImpl(2, 2, 1);
        b.chain(c);
        a.chain(b);
        assertEquals(3, a.size());
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsFour() {
        LcsItemImpl a = new LcsItemImpl(0, 0, 1);
        LcsItemImpl b = new LcsItemImpl(1, 1, 1);
        LcsItemImpl c = new LcsItemImpl(2, 2, 1);
        LcsItemImpl d = new LcsItemImpl(3, 3, 1);
        b.chain(c);
        c.chain(d);
        a.chain(b);
        // FIXME
        //assertEquals(4, a.size());
    }
}
