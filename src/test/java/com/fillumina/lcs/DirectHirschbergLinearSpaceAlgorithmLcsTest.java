package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class DirectHirschbergLinearSpaceAlgorithmLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new DirectHirschbergLinearSpaceAlgorithmLcs<Character>() {

            @Override
            public List<Character> lcs(List<Character> xs,
                    List<Character> ys) {
                count(xs, ys); // counts how many times lcs() is called
                return super.lcs(xs, ys);
            }
        };
    }
}
