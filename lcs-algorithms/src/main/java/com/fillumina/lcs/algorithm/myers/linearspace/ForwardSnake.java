package com.fillumina.lcs.algorithm.myers.linearspace;

/**
 * The first element of a forward snake (xStart, yStart) is a pair of not matching
 * elements. Its diagonal (sequence of matching elements) goes from
 * xMid,yMid to xEnd,yEnd.
 * A forward snake is generated during the execution of the forward Myers
 * algorithm.
 *
 * @author Francesco Illuminati
 */
class ForwardSnake extends Snake {

    public ForwardSnake(int xStart, int yStart, int xMid, int yMid, int xEnd,
            int yEnd) {
        super(xStart, yStart, xMid, yMid, xEnd, yEnd);
    }

    @Override
    public Interval getDiagonal() {
        return new Interval(xMid, getxEnd());
    }
}
