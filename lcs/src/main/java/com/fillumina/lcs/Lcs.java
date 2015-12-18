package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Lcs {

    List<LcsItem> calculateLcs(LcsInput lcsInput);

    int calculateLcsLength(LcsInput lcsInput);
}
