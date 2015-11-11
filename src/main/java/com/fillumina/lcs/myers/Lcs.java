package com.fillumina.lcs.myers;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs {
    boolean equals(int x, int y);

    int getFirstSequenceLength();

    int getSecondSequenceLength();

    Match getMatch();
}
