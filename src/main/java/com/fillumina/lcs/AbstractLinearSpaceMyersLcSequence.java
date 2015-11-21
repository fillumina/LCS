package com.fillumina.lcs;

import java.util.Collection;

/**
 * Implementation of the Linear Space Myers LCS algorithm. For maximum
 * flexibility its input is provided by extending the class.
 * It returns an ordered sequence of {@link LcsItemImpl}s.
 * Note that this class can be used for one calculation only.
 *
 * @see LinearSpaceMyersLcs
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLinearSpaceMyersLcSequence
        extends AbstractLinearSpaceMyersLcIndex {

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
        return ((LcsItemImpl)head).size();
    }

    public Collection<LcsItem> getLcs() {
        return ((LcsItemImpl)head);
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
