package com.fillumina.distance;

/**
 *
 * @author Francesco Illuminati 
 */
public class OptimizedStringHjelmqvistDistance
        extends AbstractWagnerFischerLevenshteinDistance {
    private final String a, b;

    public static int distance(final String a, final String b) {
        return new OptimizedStringHjelmqvistDistance(a, b).distance();
    }

    private OptimizedStringHjelmqvistDistance(String a, String b) {
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
