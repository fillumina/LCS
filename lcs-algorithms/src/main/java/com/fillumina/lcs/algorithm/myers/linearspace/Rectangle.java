package com.fillumina.lcs.algorithm.myers.linearspace;

/**
 * A 2D area described by the top,left - bottom,right coordinates.
 */
class Rectangle {
    private final int xStart;
    private final int yStart;
    private final int xEnd;
    private final int yEnd;
    protected final int n;
    protected final int m;

    public Rectangle(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.n = xEnd - xStart;
        this.m = yEnd - yStart;
    }

    /** Has zero width or height. */
    boolean isImproper() {
        return xStart == xEnd || yStart == yEnd;
    }

    public int getxStart() {
        return xStart;
    }

    public int getyStart() {
        return yStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyEnd() {
        return yEnd;
    }

    /** The given rectangle is coincident with this. */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Rectangle other = (Rectangle) obj;
        return xStart == other.xStart &&
                yStart == other.yStart &&
                xEnd == other.xEnd &&
                yEnd == other.yEnd;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.xStart;
        hash = 59 * hash + this.yStart;
        hash = 59 * hash + this.xEnd;
        hash = 59 * hash + this.yEnd;
        return hash;
    }
}
