package com.fillumina.lcs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CountingListener<T> implements LcsCallListener<T> {
    private final Map<String, Integer> countingMap = new HashMap<>();

    @Override
    public void notifyLcs(List<T> xs, List<T> ys) {
        count(xs.toString() + ys.toString());
    }

    private void count(String s) {
        Integer num = countingMap.get(s);
        if (num == null) {
            num = 0;
        }
        countingMap.put(s, num + 1);
    }

    @Override
    public String toString() {
        Map<Integer,String> frequencyMap = calculateFrequencyMap();
        long counter = 0;
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<Integer,String> entry : frequencyMap.entrySet()) {
            final Integer value = entry.getKey();
            counter += value;
            buf.append(value).append(":\t").append(entry.getValue())
                    .append('\n');
        }
        buf.append("--------\n").append("TOTAL:\t").append(counter).append('\n');
        return buf.toString();
    }

    private Map<Integer, String> calculateFrequencyMap() {
        final Map<Integer,String> map = new TreeMap<>(
                new Comparator<Integer>() {

                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(o2, o1);
                    }

                });
        for (Map.Entry<String,Integer> entry : countingMap.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        return map;
    }
}
