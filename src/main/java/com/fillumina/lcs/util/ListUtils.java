package com.fillumina.lcs.util;

import java.util.ArrayList;
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

    public static <T> List<T> add(List<T> a, List<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        List<T> l = new ArrayList<>(a);
        l.addAll(b);
        return l;
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

    public static <T> List<T> reverse(final List<T> list) {
        List<T> l = new ArrayList<>(list);
        Collections.reverse(l);
        return l;
    }
}
