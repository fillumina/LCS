package com.fillumina.lcs.hirschberg;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.ListLcs;
import com.fillumina.lcs.hirschberg.DirectHirschbergLinearSpaceLcs;
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
