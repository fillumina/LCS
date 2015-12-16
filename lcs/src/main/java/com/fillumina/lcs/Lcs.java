package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs {

    /**
     * Perform the calculation.
     *
     * @see CollectionLcsInput
     * @see LcsItemSequencer
     */
    List<LcsItem> calculateLcs(final LcsInput lcsInput,
            final LcsSequencer lcsSequencer);
}
