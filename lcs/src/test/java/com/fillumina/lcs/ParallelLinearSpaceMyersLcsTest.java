package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ParallelLinearSpaceMyersLcsTest
        extends AbstractLcsLengthTest {

    public static void main(String[] args) {
        new ParallelLinearSpaceMyersLcsTest().randomLcs(60, 10, 100);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends LcsList & LcsLength> T getLcsSequenceGenerator() {
        return (T) new LcsAdaptor(ParallelLinearSpaceMyersLcs.INSTANCE);
    }
}
