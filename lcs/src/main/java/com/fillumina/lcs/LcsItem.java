package com.fillumina.lcs;

import java.io.Serializable;

/**
 * Item of the LCS sequence.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface LcsItem extends Serializable {

    /**
     * @return the index in the first sequence from which the match starts.
     */
    int getFirstSequenceIndex();

    /**
     * @return the index in the second sequence from which the match starts.
     */
    int getSecondSequenceIndex();

    /** @return how many subsequent indexes matches. */
    int getSteps();

    /**
     * @return an iterable of matching indexes on the first sequence
     * starting from the present match.
     */
    Iterable<Integer> lcsIndexesOfTheFirstSequence();

    /**
     * @return an iterable of matching indexes on the second sequence
     * starting from the present match.
     */
    Iterable<Integer> lcsIndexesOfTheSecondSequence();
}
