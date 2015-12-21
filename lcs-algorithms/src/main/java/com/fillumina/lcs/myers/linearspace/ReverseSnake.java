package com.fillumina.lcs.myers.linearspace;

/**
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
