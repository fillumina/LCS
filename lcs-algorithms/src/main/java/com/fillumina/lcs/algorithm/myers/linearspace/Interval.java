package com.fillumina.lcs.algorithm.myers.linearspace;

import java.util.Iterator;

/**
 * An iterable over integers defined by its boundaries.
 *
 * @author Francesco Illuminati
 */
class Interval implements Iterable<Integer> {

    static final Interval EMPTY = new Interval(0, 0);
    private final int start;
    private final int end;

    /**
     * Creates an interval.
     * 
     * @param start start of interval (included)
     * @param end   end of interval (excluded)
     */
    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean isEmty() {
        return start >= end;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int current = start;

            @Override
            public boolean hasNext() {
                return current < end;
            }

            @Override
            public Integer next() {
                int tmp = current;
                current++;
                return tmp;
            }
        };
    }

}
