package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BaselineOptimizedLinearSpaceMyersLcsTest extends AbstractLcsTest {

    private OptimizedLinearSpaceMyersLcs<Character> algo = new OptimizedLinearSpaceMyersLcs<>();

    public static void main(String[] args) {
        new BaselineOptimizedLinearSpaceMyersLcsTest().randomLcs(60, 10);
    }

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new OptimizedLinearSpaceMyersLcs<>();
    }
}
