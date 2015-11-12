package com.fillumina.lcs.testutil;

import com.fillumina.lcs.util.ListUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RandomSequenceGenerator {

    private final int total, lcs;
    private final long seed;
    private final List<Integer> lcsList, a, b;

    public static void main(String[] args) {
        new PerformanceTest().executeWithIntermediateOutput();
    }

    /**
     *
     * @param total length of the sequences
     * @param lcs   lenght of the longest common sequence
     */
    public RandomSequenceGenerator(final int total, final int lcs) {
        this(total, lcs, System.nanoTime());
    }

    /**
     *
     * @param total length of the sequences
     * @param lcs   lenght of the longest common sequence
     * @param seed  seed of the random number generator. Equal seeds mean
     *              same random sequence is repeated (useful for debugging
     *              a specific situation).
     */
    public RandomSequenceGenerator(final int total, final int lcs,
            final long seed) {
        this.total = total;
        this.lcs = lcs;
        this.seed = seed;

        List<Integer> aList = new ArrayList<>(total);
        List<Integer> bList = new ArrayList<>(total);
        createSequences(aList, bList, total);

        this.lcsList = createLcsList(new ArrayList<Integer>(lcs), lcs);

        // useful to regenerate a failed test (just pass the same seed)
        System.err.println("seed: " + seed);
        Random rnd = new Random(seed);
        this.a = setLcsSequenceRandomlyIntoList(aList, lcsList, rnd);
        this.b = setLcsSequenceRandomlyIntoList(bList, lcsList, rnd);
    }

    private static void createSequences(final List<Integer> a,
            final List<Integer> b,
            final int total) {
        int index;
        for (int i = 0; i < total; i++) {
            index = i * 2;
            a.add(index);
            b.add(index+1);
        }
    }

    private static List<Integer> createLcsList(final List<Integer> lcsList,
            final int lcs) {
        for (int i = 1; i <= lcs; i++) {
            lcsList.add(-i);
        }
        return Collections.unmodifiableList(lcsList);
    }

    private List<Integer> setLcsSequenceRandomlyIntoList(final List<Integer> list,
            final List<Integer> lcsList, final Random rnd) {
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
            list.set(i.next(), lcsList.get(index));
            index++;
        }
        return Collections.unmodifiableList(list);
    }

    public List<Integer> getLcsList() {
        return lcsList;
    }

    public List<Integer> getA() {
        return a;
    }

    public List<Integer> getB() {
        return b;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "RandomSequenceGenerator (lcs= " + lcs +
                ", seed= " + seed + "L ):\n" +
                " a=" + ListUtils.toString(a) +
                "\n b=" + ListUtils.toString(b) +
                "\n";
    }
}
