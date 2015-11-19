package com.fillumina.lcs.myers.docx4j;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import org.junit.Test;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class IbmLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new IbmLcs();
    }

    @Test(timeout = 1_000L)
    public void shouldPassVeryLongTest() {
        randomLcs(6000, 5000, 1);
    }
}
