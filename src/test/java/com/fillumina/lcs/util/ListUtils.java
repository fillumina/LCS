package com.fillumina.lcs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The methods in this class doesn't modify the arguments.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ListUtils {

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

    public static String toString(final Iterable<?> iterable) {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        boolean first = true;
        for (Object o : iterable) {
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(Objects.toString(o));
        }
        buf.append("]");
        return buf.toString();
    }

    public static String toLines(final Iterable<?> iterable) {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (Object o : iterable) {
            if (first) {
                first = false;
            } else {
                buf.append("\n");
            }
            buf.append(Objects.toString(o));
        }
        buf.append("\n");
        return buf.toString();
    }
}
