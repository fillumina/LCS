package com.fillumina.lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface LcsSequencer {

    /**
     * Notifies about the found matches and eventually creates a new
     * {@link LcsItem}.
     *
     * @param x     first index on the first sequence
     * @param y     first index on the second sequence
     * @param steps number of subsequent indexes that match
     * @return      a {@link LcsItem} that should be composed with
     *              {@link #chain(LcsItem, LcsItem, LcsItem) }. If you are not
     *              interested in an ordered chain of matches you can simply
     *              return {@code null}.
     */
    LcsItem match(int x, int y, int steps);

    /** Chains {@link LcsItem}s. */
    LcsItem chain(LcsItem before, LcsItem middle, LcsItem after);
}
