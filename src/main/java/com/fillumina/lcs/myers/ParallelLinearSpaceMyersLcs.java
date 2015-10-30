package com.fillumina.lcs.myers;

import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.myers.ALinearSpaceMyersLcs.Match;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * The indexes are passed along the calls so to avoid using sublists.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ParallelLinearSpaceMyersLcs<T>
        extends ALinearSpaceMyersLcs<T> {

    @Override
    public List<T> lcs(final List<T> a, final List<T> b) {
        Match matches = lcsMain(a, b);
        return matches.extractLcsForFirstSequence(a);
    }

    @Override
    protected Match lcsRec(List<T> a, int a0, int n,
            List<T> b, int b0, int m, int[][] vv) {
        LcsTask t1 = new LcsTask(a0, n, b0, m);
        return super.lcsRec(a, a0, n, b, b0, m, vv);
    }

    private class LcsTask extends RecursiveTask<Match> {
        private static final long serialVersionUID = 1L;
        private final int a0, n, b0, m;

        public LcsTask(int a0, int n, int b0, int m) {
            this.a0 = a0;
            this.n = n;
            this.b0 = b0;
            this.m = m;
        }

        @Override
        protected Match compute() {
            return null;
        }

    }
}
