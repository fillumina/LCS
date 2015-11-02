package com.fillumina.lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class NotRecursiveLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new NotRecursiveLcs<>();
    }
}
