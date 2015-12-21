package com.fillumina.distance;

/**
 * An implementation of the {@link AbstractHjelmqvistLevenshteinDistance}
 * to return distance between strings. It's very fast and memory efficient.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class StringHjelmqvistLevenshteinDistance
        extends AbstractHjelmqvistLevenshteinDistance {
    private final String a, b;

    public static int distance(final String a, final String b) {
        return new StringHjelmqvistLevenshteinDistance(a, b).distance();
    }

    private StringHjelmqvistLevenshteinDistance(String a, String b) {
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
