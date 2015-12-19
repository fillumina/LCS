package com.fillumina.lcs;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.helper.LcsLength;
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
    @SuppressWarnings("unchecked")
    public <T extends LcsList & LcsLength> T getLcsSequenceGenerator() {
        return (T) new LcsAdaptor(MyersLcs.INSTANCE);
    }
}
