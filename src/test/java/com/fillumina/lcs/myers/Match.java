package com.fillumina.lcs.myers;

import com.fillumina.lcs.*;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class Match implements Iterable<Match>, Serializable {
    private static final long serialVersionUID = 1L;

    public static final Match NULL = new Match(-1, -1, 0);

    private final int x;
    private final int y;
    private final int steps;
    private Match next;
    private Match last;

    public Match(int x, int y, int steps) {
        this.x = x;
        this.y = y;
        this.steps = steps;
    }

    /**
     * This is NOT a general chain algorithm, it works because the way
     * matches are generated in the LCS algorithms.
     */
    Match chain(final Match other) {
        Match current = this;
        if (last != null) {
            current = last;
        }
        current.next = other;
        accumulateLcs(other.getLcs());
        if (other.last != null) {
            current = other.last;
        } else {
            current = other;
        }
        last = current;
        return this;
    }

    protected void accumulateLcs(int otherLcs) {
        // do nothing
    }

    public int getLcs() {
        int lcs = 0;
        for (Match m : this) {
            lcs += m.getSteps();
        }
        return lcs;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSteps() {
        return steps;
    }

    public Iterable<Integer> lcsIndexes() {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private Iterator<Match> i = Match.this.iterator();
                    private Match current;
                    private int step = 0;

                    @Override
                    public boolean hasNext() {
                        while (current == null || current.steps == 0 ||
                                (step + 1) == current.steps) {
                            if (i.hasNext()) {
                                current = (Match) i.next();
                                step = -1;
                            } else {
                                return false;
                            }
                        }
                        step++;
                        return true;
                    }

                    @Override
                    public Integer next() {
                        return current.x + step;
                    }
                };
            }
        };
    }

    @Override
    public Iterator<Match> iterator() {
        return new Iterator<Match>() {
            private Match current = Match.this;

            @Override
            public boolean hasNext() {
                return current != null && current != NULL;
            }

            @Override
            public Match next() {
                Match tmp = current;
                current = current.next;
                return tmp;
            }
        };
    }

    @Override
    public String toString() {
        if (this == NULL) {
            return "Match{NULL}";
        }
        return getClass().getSimpleName() +
                "{xStart=" + x + ", yStart=" + y +
                ", steps=" + steps + '}';
    }

    public static Match chain(Match before, Match middle, Match after) {
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

    /**
     * @return a string representation of the entire iterable.
     */
    public static String toString(Match matches) {
        StringBuilder buf = new StringBuilder("[");
        for (Match m : matches) {
            buf.append(", ").append(m.toString());
        }
        buf.append("]");
        return buf.toString();
    }
}
