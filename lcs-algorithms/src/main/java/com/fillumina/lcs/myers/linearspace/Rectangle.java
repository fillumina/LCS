package com.fillumina.lcs.myers.linearspace;

import java.util.List;

/**
 * A 2D area described by the top,left - bottom,right coordinates
 * considering the origin on the top-left.
 */
class Rectangle {
    private final int xStart;
    private final int yStart;
    private final int xEnd;
    private final int yEnd;
    protected final int n;
    protected final int m;

    public Rectangle(List<?> a, List<?> b) {
        this(0, 0, a.size(), b.size());
    }

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

    public boolean isMaximum(Rectangle other) {
        return xStart == other.xStart &&
                yStart == other.yStart &&
                xEnd == other.xEnd &&
                yEnd == other.yEnd;
    }

}
