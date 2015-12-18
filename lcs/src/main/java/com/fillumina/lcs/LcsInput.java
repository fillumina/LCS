package com.fillumina.lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface LcsInput {

    /** Override to return the length of the first sequence. */
    int getFirstSequenceLength();

    /** Override to return the length of the second sequence. */
    int getSecondSequenceLength();

    /**
     * Provides the equality check.
     *
     * @param x the index in the first sequence
     * @param y the index in the second sequence
     * @return {@code true} if the item with index x on the first sequence
     *          is equal to the item with index y on the second sequence.
     */
    boolean equals(int x, int y);
}
