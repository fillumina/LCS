package com.fillumina.lcs.algorithm.myers.linearspace;

import com.fillumina.lcs.helper.LcsList;
import java.util.List;

/**
 * The {@link LinearSpaceMyersLcsSolver} uses an internal state and so it
 * cannot be used stateless as suggested by the {@link LcsList} interface. This
 * adaptor instantiates a new {@link LinearSpaceMyersLcsSolver} at each
 * call of {@link #lcs(List,List)} complying with the interface.
 *
 * @author Francesco Illuminati
 */
public class RLinearSpaceMyersLcs implements LcsList {

    @Override
    public <T> List<? extends T> lcs(List<? extends T> a, List<? extends T> b) {
        return new LinearSpaceMyersLcsSolver<>(a, b).calculateLcs();
    }
}
