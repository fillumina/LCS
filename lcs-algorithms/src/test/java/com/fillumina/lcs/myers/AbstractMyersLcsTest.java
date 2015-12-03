package com.fillumina.lcs.myers;

import com.fillumina.lcs.*;
import com.fillumina.lcs.myers.AbstractMyersLcsAdaptor;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class AbstractMyersLcsTest extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new AbstractMyersLcsTest().randomLcs(60000, 50, 1);
    }

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new AbstractMyersLcsAdaptor();
    }
}
