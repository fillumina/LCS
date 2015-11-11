package com.fillumina.lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SmithWatermanLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new SmithWatermanLcs<>();
    }
}
