package com.fillumina.distance;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class StringLevenshteinDistance
        extends AbstractWagnerFischerLevenshteinDistance {
    private final String a, b;

    public static int distance(final String a, final String b) {
        return new StringLevenshteinDistance(a, b).distance();
    }

    private StringLevenshteinDistance(String a, String b) {
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
