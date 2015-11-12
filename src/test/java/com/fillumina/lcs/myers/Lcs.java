package com.fillumina.lcs.myers;

import com.fillumina.lcs.Match;

/**
 * This is a more flexible interface for an LCS algorithm. It allows to
 * select LCS on sequences different from lists.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs {
    boolean equals(int x, int y);

    int getFirstSequenceLength();

    int getSecondSequenceLength();

    Match getMatch();
}
