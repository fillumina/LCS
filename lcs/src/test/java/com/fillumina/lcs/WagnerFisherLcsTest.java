package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class WagnerFisherLcsTest extends AbstractLcsLengthTest  {

    @Override
    public LcsLength getLcsLengthAlgorithm() {
        return new LcsLengthAdaptor(WagnerFischerLcs.INSTANCE);
    }

}
