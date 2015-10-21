package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.AbstractLcsTestExecutor;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.LinearSpaceMyersLcs;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new LinearSpaceMyersLcs<>();
    }
}
