package com.fillumina.lcs.hirschberg;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.ListLcs;
import com.fillumina.lcs.hirschberg.RListHirschbergLinearSpaceAlgorithmLcs;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RListHirschbergLinearSpaceAlgorithmLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new RListHirschbergLinearSpaceAlgorithmLcs<Character>() {

            @Override
            public List<Character> lcs(List<Character> xs,
                    List<Character> ys) {
                count(xs, ys);
                return super.lcs(xs, ys);
            }
        };
    }

    @Test
    public void shouldGetTheRightResult() {
        lcs("PONY", "PYTHON")
            .assertResult("PON");
//            .printerr();
    }
}
