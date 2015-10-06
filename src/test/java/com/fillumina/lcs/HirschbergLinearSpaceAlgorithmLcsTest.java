package com.fillumina.lcs;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class HirschbergLinearSpaceAlgorithmLcsTest {

    @Test
    public void shouldGetTheRightResult() {

        new AbstractLcsTestExecutor() {

            @Override
            protected Lcs<Character> getLcs() {
                return new HirschbergLinearSpaceAlgorithmLcs<Character>() {

                    @Override
                    public List<Character> lcs(List<Character> xs,
                            List<Character> ys) {
                        count(xs, ys);
                        return super.lcs(xs, ys);
                    }
                };
            }

        }.lcs("PONY", "PYTHON")
                .assertResult("PON")
                .printerr();
    }
}
