package com.fillumina.lcs;

import java.util.Collection;

/**
 * Implementation of the Linear Space Myers LCS algorithm. Use this class
 * if you need an ordered list of indexes or the LCS as a list.
 *
 * @see LinearSpaceMyersLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLinearSpaceMyersLcSequence
        extends AbstractLinearSpaceMyersLcIndex {
    private LcsItemImpl head;

    /** Perform the calculation. */
    @Override
    protected void calculateLcs() {
        final int n = getFirstSequenceLength();
        final int m = getSecondSequenceLength();
        if (n == 0 || m == 0) {
            head = LcsItemImpl.NULL;
            return;
        }
        final LcsItem lcsItem = lcsTail(0, n, 0, m);
        head = (LcsItemImpl)
                (lcsItem == null ? LcsItemImpl.NULL : lcsItem);
    }

    /**
     * @return an iterable of matching indexes on the first sequence
     * starting from the present match.
     */
    public Iterable<Integer> lcsIndexesOfTheFirstSequence() {
        return head.lcsIndexesOfTheFirstSequence();
    }

    /**
     * @return an iterable of matching indexes on the second sequence
     * starting from the present match.
     */
    public Iterable<Integer> lcsIndexesOfTheSecondSequence() {
        return head.lcsIndexesOfTheSecondSequence();
    }

    public int getLcsLength() {
        return head.size();
    }

    public Collection<LcsItem> getLcs() {
        return head;
    }

    @Override
    protected LcsItem match(int x, int y, int steps) {
        return new LcsItemImpl(x, y, steps);
    }

    @Override
    protected LcsItem chain(final LcsItem before,
            final LcsItem middle, final LcsItem after) {
        if (middle == null) {
            if (after == null) {
                return before;
            }
            if (before == null) {
                return after;
            }
            return ((LcsItemImpl)before).chain((LcsItemImpl)after);
        }
        if (after == null) {
            if (before == null) {
                return middle;
            }
            return ((LcsItemImpl)before).chain((LcsItemImpl)middle);
        }
        if (before == null) {
            return ((LcsItemImpl)middle).chain((LcsItemImpl)after);
        }
        return ((LcsItemImpl)before).chain(
                ((LcsItemImpl)middle).chain((LcsItemImpl)after));
    }
}
