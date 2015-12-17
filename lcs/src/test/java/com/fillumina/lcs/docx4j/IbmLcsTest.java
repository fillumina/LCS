package com.fillumina.lcs.docx4j;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import org.junit.Test;
import com.fillumina.lcs.LcsList;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class IbmLcsTest extends AbstractLcsTest {

    public static void main(String[] args) {
        new IbmLcsTest().randomLcs(600, 500, 1000);
    }

    @Override
    public LcsList getLcsAlgorithm() {
        return new IbmLcs();
    }

    @Test(timeout = 1_000L)
    public void shouldPassVeryLongTest() {
        randomLcs(6000, 5000, 1);
    }
}
