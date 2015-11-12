package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.ListLcs;
import com.fillumina.lcs.scoretable.BottomUpLcs;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BottomUpLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new BottomUpLcs<>();
    }
}
