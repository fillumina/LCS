package com.fillumina.lcs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The methods in this class doesn't modify the arguments.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ListUtils {

    /** The given list is not modified. */
    public static <T> List<T> concatenate(List<T> list, T element) {
        List<T> l = new ArrayList<>(list.size() + 1);
        l.addAll(list);
        l.add(element);
        return Collections.unmodifiableList(l);
    }

    /** The given lists are not modified. */
    public static <T> List<T> concatenate(List<T> a, List<T> b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        List<T> l = new ArrayList<>(a.size() + b.size());
        l.addAll(a);
        l.addAll(b);
        return Collections.unmodifiableList(l);
    }

    public static <T> List<T> maxLenght(List<T> a, List<T> b) {
        return a.size() > b.size() ? a : b;
    }

    public static <T> List<T> reverse(final List<T> list) {
        List<T> l = new ArrayList<>(list);
        Collections.reverse(l);
        return Collections.unmodifiableList(l);
    }
}
