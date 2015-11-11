package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class DirectHirschbergLinearSpaceLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new DirectHirschbergLinearSpaceLcs<Character>() {

            @Override
            public List<Character> lcs(List<Character> xs,
                    List<Character> ys) {
                count(xs, ys); // counts how many times lcs() is called
                return super.lcs(xs, ys);
            }
        };
    }
}
