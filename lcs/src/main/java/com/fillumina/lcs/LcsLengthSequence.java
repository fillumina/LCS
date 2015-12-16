package com.fillumina.lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsLengthSequence implements LcsSequencer {
    private int counter;

    public int getLcsLength() {
        return counter;
    }

    @Override
    public LcsItem match(int x, int y, int steps) {
        counter += steps;
        return null;
    }

    @Override
    public LcsItem chain(LcsItem before, LcsItem middle, LcsItem after) {
        return null;
    }
}
