package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLcsTestExecutor
        extends CharacterLcsTestHelper {
    private final Map<String, Integer> countingMap = new HashMap<>();

    public class Result {
        private final String result;
        private final CountingResult countingResult;

        public Result(String result, CountingResult countingResult) {
            this.result = result;
            this.countingResult = countingResult;
        }

        public Result assertResult(String... results) {
            boolean success = false;
            for (String result : results) {
                success = success || result.equals(this.result);
            }
            if (!success) {
                throw new AssertionError(getName() +
                        " invalid result: " + orEmpty(this.result));
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

        public Result assertNumberOfCalls(long calls) {
            assertEquals(getName() + " wrong number of calls",
                    calls, countingResult.getNumberOfCalls());
            return this;
        }

        public void printerr() {
            System.err.println(toString());
        }

        @Override
        public String toString() {
            return countingResult.toString();
        }
    }

    private static class CountingResult {
        private final List<Map.Entry<String,Integer>> list;
        private final long numberOfCalls;

        public CountingResult(Map<String,Integer> countingMap) {
            long counter = 0;
            list = new ArrayList<>(countingMap.size());
            for (Map.Entry<String,Integer> entry : countingMap.entrySet()) {
                counter += entry.getValue();
                list.add(entry);
            }
            this.numberOfCalls = counter;
        }

        public long getNumberOfCalls() {
            return numberOfCalls;
        }

        @Override
        public String toString() {
            Collections.sort(list, new Comparator<Map.Entry<String,Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1,
                        Map.Entry<String, Integer> o2) {
                    return Integer.compare(o2.getValue(), o1.getValue());
                }
            });

            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String,Integer> entry : list) {
                buf.append(entry.getValue()).append(":\t")
                        .append(entry.getKey()).append('\n');
            };
            buf.append("\n--------\nTOTAL:\t").append(numberOfCalls).append('\n');
            return buf.toString();
        }
    }

    /**
     * Call {@link #count(List<Characters> xs, List<Charactes> ys)} in
     * the body of the created
     * {@link Lcs#lcs(java.util.List, java.util.List)}.
     */
    protected abstract Lcs<?> getLcsAlgorithm();

    @SuppressWarnings("unchecked")
    public Result lcs(final String xs, final String ys) {
        return new Result(
                executeLcs((Lcs)getLcsAlgorithm(), xs, ys),
                new CountingResult(countingMap));
    }

    protected void count(List<Character> xs, List<Character> ys) {
        count(xs.toString() + ys.toString());
    }

    private void count(String s) {
        Integer num = countingMap.get(s);
        if (num == null) {
            num = 0;
        }
        countingMap.put(s, num + 1);
    }

    private String getName() {
        final Class<? extends Lcs> clazz = getLcsAlgorithm().getClass();
        final String simpleName = clazz.getSimpleName();
        if (simpleName.isEmpty()) {
            return clazz.getSuperclass().getSimpleName();
        }
        return simpleName;
    }

}
