package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLinearSpaceMyersLcsAdaptor;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BaselineOptimizedLinearSpaceMyersLcsTest extends AbstractLcsTest {

    private AbstractLinearSpaceMyersLcsAdaptor<Character> algo = new AbstractLinearSpaceMyersLcsAdaptor<>();

    public static void main(String[] args) {
        new BaselineOptimizedLinearSpaceMyersLcsTest().randomLcs(60, 10, 100);
    }

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new AbstractLinearSpaceMyersLcsAdaptor<>();
    }
}
