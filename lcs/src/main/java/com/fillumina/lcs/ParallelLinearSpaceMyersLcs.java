package com.fillumina.lcs;

import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ParallelLinearSpaceMyersLcs extends LcsHeadTailReducer<Void>
        implements Lcs {
    public static final ParallelLinearSpaceMyersLcs INSTANCE =
            new ParallelLinearSpaceMyersLcs();

    @Override
    protected LcsItem lcs(Void obj,
            final LcsInput lcsInput,
            final LcsSequencer seqGen,
            final int a0, final int n, final int b0, final int m) {
        return new Container(lcsInput, seqGen, n + m + 1).lcs(a0, n, b0, m);
    }

    private static class Container {
        private final ThreadLocal<int[][]> cache;
        private final LcsInput lcsInput;
        private final LcsSequencer seqGen;

        public Container(final LcsInput lcsInput, final LcsSequencer seqGen,
                final int max) {
            this.lcsInput = lcsInput;
            this.seqGen = seqGen;
            cache = new ThreadLocal<int[][]>() {
                @Override
                protected int[][] initialValue() {
                    return new int[2][max << 1];
                }
            };
        }

        LcsItem lcs(int a0, int n, int b0, int m) {
            return new RecursiveLcs(a0, n, b0, m).compute();
        }

        private class RecursiveLcs extends RecursiveTask<LcsItem> {
            private static final long serialVersionUID = 1L;
            private final int a0, n, b0, m;

            public RecursiveLcs(int a0, int n, int b0, int m) {
                super();
                this.a0 = a0;
                this.n = n;
                this.b0 = b0;
                this.m = m;
            }

            @Override
            protected LcsItem compute() {
                // accessing local variables is faster
                final int a0 = this.a0;
                final int n = this.n;
                final int b0 = this.b0;
                final int m = this.m;

                if (n == 0 || m == 0) {
                    return null;
                }
                if (n == 1) {
                    if (m == 1) {
                        if (lcsInput.equals(a0, b0)) {
                            return new LcsItemImpl(a0, b0, 1);
                        }
                        return null;
                    }
                    for (int i = b0; i < b0 + m; i++) {
                        if (lcsInput.equals(a0, i)) {
                            return new LcsItemImpl(a0, i, 1);
                        }
                    }
                    return null;
                }
                if (m == 1) {
                    for (int i = a0; i < a0 + n; i++) {
                        if (lcsInput.equals(i, b0)) {
                            return new LcsItemImpl(i, b0, 1);
                        }
                    }
                    return null;
                }
                LcsItem match = null;
                int xStart = -1;
                int yStart = -1;
                int xEnd = -1;
                int yEnd = -1;
                // find middle snake
                {
                    // set variables out of scope so to have less garbage on the stack
                    final int max = (n + m + 1) / 2 + 1;
                    final int delta = n - m;
                    final boolean evenDelta = (delta & 1) == 0;
                    final int vv[][] = cache.get();
                    final int[] vf = vv[0];
                    final int[] vb = vv[1];
                    final int halfv = vf.length / 2;

                    vf[halfv + 1] = 0;
                    vb[halfv + delta - 1] = n;
                    vb[halfv - delta - 1] = n;

                    boolean isPrev;
                    int k;
                    int kDeltaStart;
                    int kDeltaEnd;
                    int prev;
                    int next;
                    int maxk;
                    int xMid;
                    int kStart = delta - 1;
                    int kEnd = delta + 1;

                    FIND_MIDDLE_SNAKE:
                    for (int d = 0; d <= max; d++) {
                        if (d > 1) {
                            kStart = delta - (d - 1);
                            kEnd = delta + (d - 1);
                        }
                        for (k = -d; k <= d; k += 2) {
                            maxk = halfv + k;
                            next = vf[maxk + 1];
                            prev = vf[maxk - 1];
                            isPrev = k == -d || (k != d && prev < next);
                            if (isPrev) {
                                xEnd = next; // down
                            } else {
                                xEnd = prev + 1; // right
                            }
                            yEnd = xEnd - k;
                            xMid = xEnd;
                            while (xEnd < n && yEnd < m &&
                                    lcsInput.equals(a0 + xEnd, b0 + yEnd)) {
                                xEnd++;
                                yEnd++;
                            }
                            vf[maxk] = xEnd;
                            if (!evenDelta && kStart <= k && k <= kEnd && xEnd >= 0 &&
                                    vb[maxk] <= xEnd) {
                                if (xEnd > xMid) {
                                    xStart = isPrev ? next : prev + 1;
                                    yStart = xStart - (k + (isPrev ? 1 : -1));
                                    match = new LcsItemImpl(a0 + xMid,
                                            b0 + (xMid - k),
                                            xEnd - xMid);
                                } else {
                                    xStart = isPrev ? next : prev;
                                    yStart = xStart - (k + (isPrev ? 1 : -1));
                                }
                                break FIND_MIDDLE_SNAKE;
                            }
                        }
                        kDeltaEnd = delta + d;
                        kDeltaStart = delta - d;
                        for (k = kDeltaStart; k <= kDeltaEnd; k += 2) {
                            maxk = halfv + k;
                            next = vb[maxk + 1];
                            prev = vb[maxk - 1];
                            isPrev = k == kDeltaEnd || (k != kDeltaStart && prev < next);
                            if (isPrev) {
                                xStart = prev; // up
                            } else {
                                xStart = next - 1; // left
                            }
                            yStart = xStart - k;
                            xMid = xStart;
                            while (xStart > 0 && yStart > 0 &&
                                    lcsInput.equals(a0 + xStart - 1,
                                    b0 + yStart - 1)) {
                                xStart--;
                                yStart--;
                            }
                            vb[maxk] = xStart;
                            if (evenDelta && -d <= k && k <= d && xStart >= 0 &&
                                    xStart <= vf[maxk]) {
                                if (xMid > xStart) {
                                    xEnd = isPrev ? prev : next - 1;
                                    yEnd = xEnd - (k + (isPrev ? -1 : 1));
                                    match = new LcsItemImpl(a0 + xStart,
                                            b0 + yStart,
                                            xMid - xStart);
                                } else {
                                    xEnd = isPrev ? prev : next;
                                    yEnd = xEnd - (k + (isPrev ? -1 : 1));
                                }
                                break FIND_MIDDLE_SNAKE;
                            }
                        }
                    }
                }
                final boolean fromStart = xStart <= 0;
                final boolean toEnd = xEnd >= n;
                if (fromStart && toEnd) {
                    return match;
                }

                LcsItem before = null;
                LcsItem after = null;
                RecursiveLcs beforeLcs = null;

                if (!fromStart)  {
                    beforeLcs = new RecursiveLcs(a0, xStart, b0, yStart);
                    if (Math.max(xStart - a0, yStart - b0) < 40) {
                        before = beforeLcs.compute();
                        beforeLcs = null;
                    } else {
                        beforeLcs.fork();
                    }
                }

                if (!(toEnd || n - xEnd == 0 || m - yEnd == 0)) {
                    after = new RecursiveLcs(a0 + xEnd, n - xEnd, b0 + yEnd, m - yEnd)
                            .compute();
                }

                if (beforeLcs != null) {
                    before = beforeLcs.join();
                }

                return seqGen.chain(before, match, after);
            }
        }
    }

}