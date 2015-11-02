package com.fillumina.lcs;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CommonOptimizationsTest extends AbstractLcsTest {

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new CommonOptimizations<>(new BottomUpLcs<Character>());
    }
}
