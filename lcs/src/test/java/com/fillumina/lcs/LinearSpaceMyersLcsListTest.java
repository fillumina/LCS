package com.fillumina.lcs;

import com.fillumina.lcs.testutil.AbstractLcsLengthTest;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsListTest extends AbstractLcsLengthTest  {

    @Override
    public LcsSizeEvaluator getLcsSequenceGenerator() {
        return new LinearSpaceMyersLcsListAdaptor();
    }

    @Test
    public void shouldReturnTheRightLength() {
        final int lcs = 500;
        final int tot = 600;
        final RandomSequenceGenerator seqGen =
                new RandomSequenceGenerator(tot, lcs);
        final LinearSpaceMyersLcs<Integer> result =
                LinearSpaceMyersLcs.lcs(seqGen.getA(), seqGen.getB());
        assertEquals(lcs, result.getLcsLength());
    }

    @Test
    public void shouldReturnTheListUsingTheSecondSequence() {
        final RandomSequenceGenerator seqGen =
                new RandomSequenceGenerator(60, 50);
        final List<Integer> firstSequence = seqGen.getA();
        final List<Integer> secondSequence = seqGen.getB();

        final LinearSpaceMyersLcs<Integer> result =
                LinearSpaceMyersLcs.lcs(firstSequence, secondSequence);

        List<Integer> firstSequenceLcs = new ArrayList<>(result.getLcsLength());
        for (int idx : result.lcsIndexesOfTheFirstSequence()) {
            firstSequenceLcs.add(firstSequence.get(idx));
        }

        List<Integer> secondSequenceLcs = new ArrayList<>(result.getLcsLength());
        for (int idx : result.lcsIndexesOfTheSecondSequence()) {
            secondSequenceLcs.add(secondSequence.get(idx));
        }

        assertEquals(firstSequenceLcs, secondSequenceLcs);
    }

    @Test
    public void shouldCheckLcs() {
        final int tot = 60;
        final int lcs = 50;
        final RandomSequenceGenerator seqGen =
                new RandomSequenceGenerator(tot, lcs);

        final List<Integer> firstSequence = seqGen.getA();
        final List<Integer> secondSequence = seqGen.getB();

        final LinearSpaceMyersLcs<Integer> result =
                LinearSpaceMyersLcs.lcs(firstSequence, secondSequence);

        int calculatedLcs = 0;
        for (LcsItem item : result.getLcs()) {
            final int steps = item.getSteps();
            calculatedLcs += steps;
        }

        assertEquals(lcs, calculatedLcs);
    }

}
