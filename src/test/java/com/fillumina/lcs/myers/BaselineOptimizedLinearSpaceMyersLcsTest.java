package com.fillumina.lcs.myers;

import com.fillumina.lcs.LinearSpaceMyersLcsAdaptor;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BaselineOptimizedLinearSpaceMyersLcsTest extends AbstractLcsTest {

    private LinearSpaceMyersLcsAdaptor<Character> algo = new LinearSpaceMyersLcsAdaptor<>();

    public static void main(String[] args) {
        new BaselineOptimizedLinearSpaceMyersLcsTest().randomLcs(60, 10);
    }

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new LinearSpaceMyersLcsAdaptor<>();
    }
}
