package com.fillumina.lcs.testutil;

import com.fillumina.lcs.helper.LcsList;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.events.Characters;

/**
 * An helper for testing LCS algorithms.
 *
 * @author Francesco Illuminati
 */
public abstract class AbstractLcsTestExecutor
        extends Converter {

    /**
     * Call {@link #count(List<Characters> xs, List<Charactes> ys)} in
     * the body of the created
     * {@link LcsList#lcs(java.util.List, java.util.List)}.
     */
    public abstract LcsList getLcsAlgorithm();

    @SuppressWarnings("unchecked")
    public Result lcs(final String xs, final String ys) {
        return new Result(executeLcs(getLcsAlgorithm(), xs, ys));
    }

    public static String executeLcs(LcsList lcs, String a, String b) {
        final Character[] aa = Converter.toArray(a);
        final Character[] bb = Converter.toArray(b);
        List<? extends Character> resultList = lcs.lcs(aa, bb);
        return toString(resultList);
    }

    private String getName() {
        final Class<?> clazz = getLcsAlgorithm().getClass();
        final String simpleName = clazz.getSimpleName();
        if (simpleName.isEmpty()) {
            return clazz.getSuperclass().getSimpleName();
        }
        return simpleName;
    }

    public class Result {
        private final String result;

        public Result(String result) {
            this.result = result;
        }

        public Result assertResult(String... results) {
            boolean success = false;
            for (String r : results) {
                if (r != null) {
                    success = success || r.equals(this.result);
                }
            }
            if (!success) {
                throw new AssertionError(getName() +
                        " invalid result: " + orEmpty(this.result) +
                        " instead of " + Arrays.toString(results));
            }
            return this;
        }

        private String orEmpty(String s) {
            if (s == null) {
                return "NULL";
            }
            if (s.isEmpty()) {
                return "EMPTY";
            }
            return "\"" + s + "\"";
        }

        public void printerr() {
            System.err.println(toString());
        }
    }
}
