package com.fillumina.lcs.myers;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.AbstractLcsTestExecutor;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.MyersLcs;
import com.fillumina.lcs.util.BidirectionalArray;
import com.fillumina.lcs.util.BidirectionalVector;
import com.fillumina.lcs.util.OneBasedVector;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class MyersLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new MyersLcs<>();
    }
}
