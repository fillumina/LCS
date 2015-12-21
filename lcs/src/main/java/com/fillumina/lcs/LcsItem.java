package com.fillumina.lcs;

import java.io.Serializable;

/**
 * Item of the LCS sequence. Note that the steps value doesn't correspond
 * to the maximum sequence of consecutive matches: there could be another
 * successive item with an element consecutive to the given one.
 *
 * @author Francesco Illuminati 
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

    /**
     * @return how many subsequent indexes matches (counting the present one).
     */
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
