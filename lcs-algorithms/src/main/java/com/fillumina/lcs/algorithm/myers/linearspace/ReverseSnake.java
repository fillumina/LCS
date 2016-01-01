package com.fillumina.lcs.algorithm.myers.linearspace;

/**
 * The last element of a reverse snake (xEnd, yEnd) is a pair of not matching
 * elements. Its diagonal (sequence of matching elements) goes from
 * xStart,yStart to xMid,yMid.
 * A reverse snake is generated during the execution of the backward Myers
 * algorithm.
 *
 * @author Francesco Illuminati
 */
class ReverseSnake extends Snake {

    public ReverseSnake(int xStart, int yStart, int xMid, int yMid, int xEnd,
            int yEnd) {
        super(xStart, yStart, xMid, yMid, xEnd, yEnd);
    }

    @Override
    public Interval getDiagonal() {
        return new Interval(getxStart(), xMid);
    }
}
