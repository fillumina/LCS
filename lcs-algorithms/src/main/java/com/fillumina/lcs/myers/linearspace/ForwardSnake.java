package com.fillumina.lcs.myers.linearspace;

/**
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
