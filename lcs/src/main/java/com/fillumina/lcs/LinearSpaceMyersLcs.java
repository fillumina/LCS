package com.fillumina.lcs;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcs implements Lcs {
    public static final LinearSpaceMyersLcs INSTANCE =
            new LinearSpaceMyersLcs();

    @Override
    public List<LcsItem> calculateLcs(LcsInput lcsInput) {
        return new Inner(false, lcsInput).calculateLcs();
    }

    @Override
    public int calculateLcsLength(LcsInput lcsInput) {
        return new Inner(true, lcsInput).calculateLcsLength();
    }

    private static class Inner extends AbstractLinearSpaceMyersLcs {
        private final LcsInput lcsInput;

        public Inner(boolean sizeOnly, LcsInput lcsInput) {
            super(sizeOnly);
            this.lcsInput = lcsInput;
        }

        @Override
        protected int getFirstSequenceLength() {
            return lcsInput.getFirstSequenceLength();
        }

        @Override
        protected int getSecondSequenceLength() {
            return lcsInput.getSecondSequenceLength();
        }

        @Override
        protected boolean sameAtIndex(int x, int y) {
            return lcsInput.equals(x, y);
        }
    }
}
