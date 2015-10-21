package com.fillumina.lcs.myers.docx4j;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.Lcs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class IbmLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<Character> getLcsAlgorithm() {
        return new IbmLcs<Character>();
    }

}
