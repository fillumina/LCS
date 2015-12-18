package com.fillumina.distance;

import com.fillumina.lcs.AbstractMyersLcs;

/**
 * This is <b>not</b> the Lovenshtein distance but it correlates with it
 * and it's pretty fast to calculate especially if the two sequences share
 * many elements.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
//TODO add some tests
public class MyersDistance extends AbstractMyersLcs {
    private final String a, b;

    public static int distance(final String a, final String b) {
        final int lcs = new MyersDistance(a, b).calculateLcsLength();
        return (a.length() + b.length() - (lcs << 1));
    }

    private MyersDistance(String a, String b) {
        this.a = a;
        this.b = b;
    }

    @Override
    protected int getFirstSequenceLength() {
        return a.length();
    }

    @Override
    protected int getSecondSequenceLength() {
        return b.length();
    }

    @Override
    protected boolean sameAtIndex(int x, int y) {
        return a.charAt(x) == b.charAt(y);
    }
}
