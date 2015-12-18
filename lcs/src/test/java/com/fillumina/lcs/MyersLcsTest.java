package com.fillumina.lcs;

import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcsTest extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new MyersLcsTest().randomLcs(600, 5, 100);
    }

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new LcsSizeEvaluatorAdaptor(MyersLcs.INSTANCE);
    }
}
