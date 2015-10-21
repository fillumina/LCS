package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.Lcs;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs0Test extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new LinearSpaceMyersLcs0<>();
    }
}
