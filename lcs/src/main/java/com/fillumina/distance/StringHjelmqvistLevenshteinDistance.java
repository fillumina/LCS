package com.fillumina.distance;

/**
 * An implementation of the {@link AbstractHjelmqvistLevenshteinDistance}
 * to return the distance between 2 strings.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">
 *  Levenshtein distance
 * </a>
 * @see <a href='http://www.codeproject.com/Articles/13525/Fast-memory-efficient-Levenshtein-algorithm'>
 *  Fast memory efficient Levenshtein algorithm (Sten Hjelmqvist)
 * </a>
 *
 * @author Francesco Illuminati
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
