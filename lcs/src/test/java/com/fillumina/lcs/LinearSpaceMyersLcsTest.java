package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;

/**
 *
 * @author Francesco Illuminati 
 */
public class LinearSpaceMyersLcsTest extends AbstractLcsLengthTest  {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends LcsList & LcsLength> T getLcsSequenceGenerator() {
        return (T) new LcsAdaptor(LinearSpaceMyersLcs.INSTANCE);
    }
}
