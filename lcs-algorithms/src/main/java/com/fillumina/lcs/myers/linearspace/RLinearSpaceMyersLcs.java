package com.fillumina.lcs.myers.linearspace;

import java.util.List;
import com.fillumina.lcs.Lcs;

/**
 * Myers algorithm that uses forward and backward snakes. It is not designed
 * to be performant but to be easy to understand.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RLinearSpaceMyersLcs implements Lcs {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        return new LinearSpaceMyersLcsSolver<>(a, b).calculateLcs();
    }
}
