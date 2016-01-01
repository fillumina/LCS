package com.fillumina.lcs;

import java.io.Serializable;

/**
 * Item containing the matching indexes of the LCS sequence. To minimize the
 * number of nodes successive items are packed together but
 * there is no assurance that an item would contain the longest sequence of
 * successive matches.
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
