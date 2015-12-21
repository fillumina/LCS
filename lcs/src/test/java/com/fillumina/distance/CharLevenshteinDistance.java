package com.fillumina.distance;


/**
 *
 * @author Francesco Illuminati 
 */
public class CharLevenshteinDistance extends AbstractWagnerFischerLevenshteinDistance {
    private final char[] a, b;

    public static int distance(final String a, final String b) {
        return new CharLevenshteinDistance(a.toCharArray(), b.toCharArray())
                .distance();
    }

    private CharLevenshteinDistance(char[] a, char[] b) {
        this.a = a;
        this.b = b;
    }

    @Override
    protected int getFirstSequenceLength() {
        return a.length;
    }

    @Override
    protected int getSecondSequenceLength() {
        return b.length;
    }

    @Override
    protected boolean sameAtIndex(int x, int y) {
        return a[x] == b[y];
    }
}
