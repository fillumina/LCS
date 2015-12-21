package com.fillumina.lcs;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati 
 */
public class LcsItemImplTest {
    private final LcsItemImpl a = new LcsItemImpl(0, 0, 1);
    private final LcsItemImpl b = new LcsItemImpl(1, 1, 1);
    private final LcsItemImpl c = new LcsItemImpl(2, 2, 1);
    private final LcsItemImpl d = new LcsItemImpl(3, 3, 1);

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

        assertSequence(head, tail);
    }

    @Test
    public void shouldChainThreeItems() {
        LcsItemImpl head = new LcsItemImpl(0, 0, 0);
        LcsItemImpl middle = new LcsItemImpl(1, 1, 1);
        LcsItemImpl tail = new LcsItemImpl(2, 2, 2);

        head.chain(middle);
        middle.chain(tail);

        assertSequence(head, middle, tail);
    }

    /** Myers algorithm always add the lowest match to a chain. */
    @Test
    public void shouldChainFourItemsInA_WRONG_WayProduceWrongResults() {
        a.chain(b);
        c.chain(d);
        b.chain(c);

        // a is not updated after adding c and b
        assertNotSame(4, a.size());

        assertSequence(a, b, c, d);
    }

    @Test
    public void shouldChainFourItemsInRightOrder() {
        c.chain(d);
        b.chain(c);
        a.chain(b);

        assertEquals(4, a.size());
        assertSequence(a, b, c, d);
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsOne() {
        assertEquals(1, a.size());
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsTwo() {
        a.chain(b);
        assertEquals(2, a.size());
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsThree() {
        b.chain(c);
        a.chain(b);
        assertEquals(3, a.size());
        assertSequence(a, b, c);
    }

    @Test
    public void shouldGetTheSequenceLengthIfLenghtIsFour() {
        c.chain(d);
        b.chain(c);
        a.chain(b);

        assertEquals(4, a.size());
        assertSequence(a, b, c , d);
    }

    @Test
    public void shouldReturnAString() {
        assertEquals("LcsItemImpl{xStart=0, yStart=0, steps=1}", a.toString());
    }

    @Test
    public void shouldRecognizeNULL() {
        assertEquals("LcsItemImpl{NULL}", LcsItemImpl.NULL.toString());
    }

    @SuppressWarnings("unchecked")
    private void assertSequence(LcsItem... items) {
        int index = 0;
        for (LcsItem item : (Iterable<LcsItem>)items[0]) {
            assertEquals("mismatch on index " + index, items[index], item);
            index++;
        }
    }
}
