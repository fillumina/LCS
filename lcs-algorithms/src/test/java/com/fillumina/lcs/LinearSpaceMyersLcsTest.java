/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fillumina.lcs;

import com.fillumina.lcs.LcsSizeEvaluator;
import com.fillumina.lcs.testutil.AbstractLcsLengthTest;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsTest extends AbstractLcsLengthTest  {

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new LinearSpaceMyersLcsAdaptor();
    }

    @Test
    public void shouldReturnTheRightLength() {
        final int lcs = 500;
        final int tot = 600;
        final RandomSequenceGenerator seqGen =
                new RandomSequenceGenerator(tot, lcs);
        final LinearSpaceMyersLcs result =
                LinearSpaceMyersLcs.lcs(seqGen.getA(), seqGen.getB());
        assertEquals(lcs, result.getLcsLength());
    }
}
