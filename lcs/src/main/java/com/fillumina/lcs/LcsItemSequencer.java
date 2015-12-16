package com.fillumina.lcs;


/**
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsItemSequencer implements LcsSequencer {
    public static final LcsItemSequencer INSTANCE = new LcsItemSequencer();

    @Override
    public LcsItem match(int x, int y, int steps) {
        return new LcsItemImpl(x, y, steps);
    }

    @Override
    public LcsItem chain(final LcsItem before,
            final LcsItem middle, final LcsItem after) {
        return LcsItemImpl.chain((LcsItemImpl)before,
                (LcsItemImpl)middle, (LcsItemImpl)after);
    }
}
