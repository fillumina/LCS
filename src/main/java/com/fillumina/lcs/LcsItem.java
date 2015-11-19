package com.fillumina.lcs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a linked list of ordered matches. The head of the chain
 * (the element returned by
 * {@link AbstractLinearSpaceMyersLcs#calculateLcs() }), can be queried
 * about the total length of the LCS (the same information in other items
 * is inaccurate) using {@link LcsItem#getSequenceSize() }.
 */
// TODO test this!
public class LcsItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final LcsItem NULL = new LcsItem(-1, -1, 0);
    private final int x;
    private final int y;
    private final int steps;
    private LcsItem next;
    private LcsItem last;
    private int lcs;

    LcsItem(int x, int y, int steps) {
        this.x = x;
        this.y = y;
        this.steps = steps;
        this.lcs = steps;
    }

    /**
     * This is NOT a general chain algorithm, it works because the way
     * matches are generated in the Myers LCS algorithms.
     */
    LcsItem chain(final LcsItem other) {
        LcsItem current = this;
        if (last != null) {
            current = last;
        }
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
    public int getSequenceSize() {
        return lcs;
    }

    /**
     * @return the index in the first sequence from which the match starts.
     */
    public int getFirstSequenceIndex() {
        return x;
    }

    /**
     * @return the index in the second sequence from which the match starts.
     */
    public int getSecondSequenceIndex() {
        return y;
    }

    /** @return how many subsequent indexes are there. */
    public int getSteps() {
        return steps;
    }

    abstract class IndexIterator implements Iterator<Integer> {

        private final Iterator<LcsItem> i = LcsItem.this.iterator();
        protected int step = 0;
        protected LcsItem current;
        protected boolean hasNext;

        public IndexIterator() {
            increment();
        }

        protected final void increment() {
            while (current == null ||
                    current.steps == 0 ||
                    (step + 1) == current.steps) {
                if (i.hasNext()) {
                    current = (LcsItem) i.next();
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
            private LcsItem current = LcsItem.this;

            @Override
            public boolean hasNext() {
                return current != null && current != NULL;
            }

            @Override
            public LcsItem next() {
                LcsItem tmp = current;
                current = current.next;
                return tmp;
            }
        };
    }

    public String toString() {
        if (this == NULL) {
            return "Match{NULL}";
        }
        return getClass().getSimpleName() +
                "{xStart=" + x + ", yStart=" + y + ", steps=" + steps + '}';
    }

    static LcsItem chain(LcsItem before, LcsItem middle, LcsItem after) {
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
