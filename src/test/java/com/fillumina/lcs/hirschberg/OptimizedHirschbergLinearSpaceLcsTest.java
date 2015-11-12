package com.fillumina.lcs.hirschberg;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.hirschberg.OptimizedHirschbergLinearSpaceLcs;
import java.util.List;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedHirschbergLinearSpaceLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new OptimizedHirschbergLinearSpaceLcs<Character>() {

            @Override
            public List<Character> lcs(List<Character> xs,
                    List<Character> ys) {
                count(xs, ys); // counts how many times lcs() is called
                return super.lcs(xs, ys);
            }
        };
    }
}
