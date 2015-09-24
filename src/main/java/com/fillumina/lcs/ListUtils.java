package com.fillumina.lcs;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ListUtils {

    public static <T> List<T> add(List<T> list, T element) {
        list.add(element);
        return list;
    }

    public static <T> List<T> maxLenght(List<T> a, List<T> b) {
        return a.size() > b.size() ? a : b;
    }

    public static <T> List<T> allButLastElementSublist(List<T> list) {
        int size = list.size();
        if (size == 0) {
            return Collections.<T>emptyList();
        }
        return list.subList(0, size - 1);
    }

    public static <T> T getLastElement(List<T> list) {
        return list.get(list.size() - 1);
    }
}
