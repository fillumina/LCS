package com.fillumina.lcs;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati 
 */
public class OptimizedLinearSpaceMyersLcs extends AbstractLinearSpaceMyersLcs {
    private final Object[] a;
    private final Object[] b;

    public OptimizedLinearSpaceMyersLcs (List<?> a, List<?> b) {
        super(true);
        this.a = a.toArray();
        this.b = b.toArray();
    }

    @Override
    protected int getFirstSequenceLength() {
        return a.length;
    }

    @Override
    protected int getSecondSequenceLength() {
        return b.length;
    }

    @Override
    protected boolean sameAtIndex(int x, int y) {
        return Objects.equals(a[x], b[y]);
    }
}
