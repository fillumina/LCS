package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati
 */
public class MyersLcsTest extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new MyersLcsTest().randomLcs(600, 5, 100);
    }

    @Override
    public LcsLength getLcsLengthAlgorithm() {
        return new LcsLengthAdaptor(MyersLcs.INSTANCE);
    }
}
