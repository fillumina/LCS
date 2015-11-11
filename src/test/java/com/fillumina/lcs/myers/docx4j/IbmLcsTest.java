package com.fillumina.lcs.myers.docx4j;

import com.fillumina.lcs.AbstractLcsTest;
import com.fillumina.lcs.RandomSequenceGenerator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fillumina.lcs.ListLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class IbmLcsTest extends AbstractLcsTest {

    @Override
    protected ListLcs<?> getLcsAlgorithm() {
        return new IbmLcs<>();
    }

    @Test(timeout = 1_000L)
    public void shouldPassVeryLongTest() {
        final int MAX = 6000;
        final int LCS = 5000;
        RandomSequenceGenerator generator =
//                new RandomSequenceGenerator(60,10, 26641683514364L);
                new RandomSequenceGenerator(MAX,LCS);

        System.out.println(generator.toString());

        @SuppressWarnings("unchecked")
        List<Integer> lcsList = ((ListLcs)getLcsAlgorithm())
                .lcs(generator.getA(), generator.getB());

        final List<Integer> result = generator.getLcsList();
//        for (int i=0; i<LCS; i++) {
//            if (result.get(i) != lcsList.get(i)) {
//                System.out.println("ERROR ON INDEX=" + i +
//                        " result=" + result.get(i) + " lcs=" + lcsList.get(i));
//                return;
//            }
//        }

        assertEquals(result, lcsList);
    }
}
