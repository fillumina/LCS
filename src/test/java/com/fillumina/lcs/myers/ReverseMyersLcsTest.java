package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.ListLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ReverseMyersLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new ReverseMyersLcs<>();
    }

}
