package com.fillumina.lcs;

import java.io.Serializable;
import java.util.Collections;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface LcsItem extends Serializable {
//    LcsItem NULL = new LcsItem() {
//        private static final long serialVersionUID = 1L;
//
//        @Override public int getFirstSequenceIndex() { return -1; }
//        @Override public int getSecondSequenceIndex() { return -1; }
//        @Override public int getSteps() { return 0; }
//
//        @Override
//        public Iterable<Integer> lcsIndexesOfTheFirstSequence() {
//            return Collections.<Integer>emptyList();
//        }
//
//        @Override
//        public Iterable<Integer> lcsIndexesOfTheSecondSequence() {
//            return Collections.<Integer>emptyList();
//        }
//    };

    /**
     * @return the index in the first sequence from which the match starts.
     */
    int getFirstSequenceIndex();

    /**
     * @return the index in the second sequence from which the match starts.
     */
    int getSecondSequenceIndex();

    /** @return how many subsequent indexes are there. */
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
