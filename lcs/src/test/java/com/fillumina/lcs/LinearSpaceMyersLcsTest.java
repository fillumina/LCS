package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati
 */
public class LinearSpaceMyersLcsTest extends AbstractLcsLengthTest  {

    @Override
    public LcsLength getLcsLengthAlgorithm() {
        return new LcsLengthAdaptor(LinearSpaceMyersLcs.INSTANCE);
    }
}
