package com.fillumina.lcs;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Item of a linked list of ordered LCS matches.
 * {@link LcsItem}s are created by the Myers algorithm by always adding a lower
 * match to a chain of matches. Because of that the first element of the chain
 * (the head) is always the last added one and so it contains an updated
 * LCS length. Because this implementation is so tied with the internals of
 * the Myers algorithm it cannot be part of the public API.
 */
class LcsItemImpl extends AbstractList<LcsItem>
        implements LcsItem {
    private static final long serialVersionUID = 1L;
    static final LcsItemImpl NULL = new LcsItemImpl(-1, -1, 0);
    private final int x;
    private final int y;
    private final int steps;
    private LcsItemImpl next;
    private LcsItemImpl last;
    private int lcs;

    LcsItemImpl(int x, int y, int steps) {
        this.x = x;
        this.y = y;
        this.steps = steps;
        this.lcs = steps;
    }

    /**
     * This is NOT a general chain algorithm, it works because the way
     * matches are generated in the Myers LCS algorithms.
     * @return always return {@code this}.
     */
    LcsItemImpl chain(final LcsItemImpl other) {
        LcsItemImpl current = (last != null) ? last : this;
        current.next = other;
        lcs += other.lcs;
        if (other.last != null) {
            current = other.last;
        } else {
            current = other;
        }
        last = current;
        return this;
    }

    /** The value is valid only for the head item of the sequence. */
    @Override
    public int size() {
        return lcs;
    }

    /**
     * @return the index in the first sequence from which the match starts.
     */
    @Override
    public int getFirstSequenceIndex() {
        return x;
    }

    /**
     * @return the index in the second sequence from which the match starts.
     */
    @Override
    public int getSecondSequenceIndex() {
        return y;
    }

    /** @return how many subsequent indexes are there. */
    @Override
    public int getSteps() {
        return steps;
    }

    /**
     * Being a linked list accessing elements with this method is very
     * inefficient. Use iterators instead.
     */
    @Override
    public LcsItem get(int index) {
        LcsItemImpl current = this;
        for (int i=0; i<index; i++) {
            current = current.next;
        }
        return current;
    }

    abstract class IndexIterator implements Iterator<Integer> {

        private final Iterator<LcsItem> i = LcsItemImpl.this.iterator();
        protected int step = 0;
        protected LcsItemImpl current;
        protected boolean hasNext;

        public IndexIterator() {
            increment();
        }

        protected final void increment() {
            while (current == null ||
                    current.steps == 0 ||
                    (step + 1) == current.steps) {
                if (i.hasNext()) {
                    current = (LcsItemImpl) i.next();
                    step = -1;
                } else {
                    hasNext = false;
                    return;
                }
            }
            step++;
            hasNext = true;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }
    }

    /**
     * @return an iterable of matching indexes on the first sequence
     * starting from the present match.
     */
    @Override
    public Iterable<Integer> lcsIndexesOfTheFirstSequence() {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new IndexIterator() {
                    @Override
                    public Integer next() {
                        if (!hasNext) {
                            throw new NoSuchElementException();
                        }
                        final int result = current.x + step;
                        increment();
                        return result;
                    }
                };
            }
        };
    }

    /**
     * @return an iterable of matching indexes on the second sequence
     * starting from the present match.
     */
    @Override
    public Iterable<Integer> lcsIndexesOfTheSecondSequence() {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new IndexIterator() {
                    @Override
                    public Integer next() {
                        if (!hasNext) {
                            throw new NoSuchElementException();
                        }
                        final int result = current.y + step;
                        increment();
                        return result;
                    }
                };
            }
        };
    }

    public Iterator<LcsItem> iterator() {
        return new Iterator<LcsItem>() {
            private LcsItemImpl current = LcsItemImpl.this;

            @Override
            public boolean hasNext() {
                return current != null && current != NULL;
            }

            @Override
            public LcsItemImpl next() {
                LcsItemImpl tmp = current;
                current = current.next;
                return tmp;
            }
        };
    }

    @Override
    public String toString() {
        final String className = getClass().getSimpleName();
        if (this == NULL) {
            return className + "{NULL}";
        }
        return className +
                "{xStart=" + x + ", yStart=" + y + ", steps=" + steps + '}';
    }

    public static LcsItemImpl chain(LcsItemImpl before,
            LcsItemImpl middle, LcsItemImpl after) {
        if (middle == null) {
            if (after == null) {
                return before;
            }
            if (before == null) {
                return after;
            }
            return before.chain(after);
        }
        if (after == null) {
            if (before == null) {
                return middle;
            }
            return before.chain(middle);
        }
        if (before == null) {
            return middle.chain(after);
        }
        return before.chain(middle.chain(after));
    }
}
