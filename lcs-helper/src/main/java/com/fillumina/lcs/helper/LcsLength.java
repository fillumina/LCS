package com.fillumina.lcs.helper;

import java.util.List;

/**
 *
 * @author Francesco Illuminati 
 */
public interface LcsLength extends LcsList {

    <T> int lcsLength(List<? extends T> xs, List<? extends T> ys);
}
