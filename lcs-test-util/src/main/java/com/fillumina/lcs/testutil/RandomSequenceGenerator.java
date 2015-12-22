package com.fillumina.lcs.testutil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Generates two random sequences of integers of the requested size each
 * containing exactly one common sequence (thus the LCS). The common
 * sequence is easily recognizable because it is composed of negative
 * numbers.
 *
 * @author Francesco Illuminati
 */
public class RandomSequenceGenerator {

    private final int total, lcs;
    private final long seed;
    private final List<Integer> lcsList;
    private Integer[] a, b;

    /**
     *
     * @param total length of the sequences
     * @param lcs   length of the longest common sequence (lcs <= total)
     */
    public RandomSequenceGenerator(final int total, final int lcs) {
        this(total, lcs, System.nanoTime());
    }

    /**
     *
     * @param total length of the sequences
     * @param lcs   length of the longest common sequence
     * @param seed  seed of the random number generator. To the same
     *              seeds correspond same random generations (useful for
     *              debugging).
     */
    public RandomSequenceGenerator(final int total, final int lcs,
            final long seed) {
        assert lcs <= total;
        this.total = total;
        this.lcs = lcs;
        this.seed = seed;

        this.a = new Integer[total];
        this.b = new Integer[total];
        createSequences(total);

        Integer[] lcsArray = createLcsArray(lcs);

        Random rnd = new Random(seed);
        setLcsSequenceRandomlyIntoList(a, lcsArray, rnd);
        setLcsSequenceRandomlyIntoList(b, lcsArray, rnd);

        this.lcsList = Arrays.asList(lcsArray);
    }

    private void createSequences(final int total) {
        int index;
        for (int i = 0; i < total; i++) {
            index = i * 2;
            a[i] = index;
            b[i] = index+1;
        }
    }

    private static Integer[] createLcsArray(final int lcs) {
        Integer[] lcsArray = new Integer[lcs];
        for (int i = 1; i <= lcs; i++) {
            lcsArray[i-1] = -i;
        }
        return lcsArray;
    }

    private void setLcsSequenceRandomlyIntoList(Integer[] array,
            final Integer[] lcsArray, final Random rnd) {
        int max = lcs;
        TreeSet<Integer> set = new TreeSet<>();
        for (int i=0; i<max; i++) {
            if (!set.add(rnd.nextInt(total))) {
                max++;
            }
        }
        assert set.size() == lcs;
        int index = 0;
        Iterator<Integer> i = set.iterator();
        while(i.hasNext()) {
            array[i.next()] = lcsArray[index];
            index++;
        }
    }

    public List<Integer> getLcs() {
        return lcsList;
    }

    public List<Integer> getA() {
        return Arrays.asList(a);
    }

    public List<Integer> getB() {
        return Arrays.asList(b);
    }

    public Integer[] getArrayA() {
        return Arrays.copyOf(a, a.length);
    }

    public Integer[] getArrayB() {
        return Arrays.copyOf(b, b.length);
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "RandomSequenceGenerator (lcs= " + lcs +
                ", seed= " + seed + "L ):\n" +
                " a=" + a.toString() +
                "\n b=" + b.toString() +
                "\n";
    }
}
