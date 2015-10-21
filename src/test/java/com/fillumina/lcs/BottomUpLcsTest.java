package com.fillumina.lcs;

import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BottomUpLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new BottomUpLcs<>();
    }
}
