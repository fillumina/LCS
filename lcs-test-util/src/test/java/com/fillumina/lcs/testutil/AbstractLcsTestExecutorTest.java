package com.fillumina.lcs.testutil;

import com.fillumina.lcs.helper.LcsList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class AbstractLcsTestExecutorTest extends AbstractLcsTestExecutor {

    @Test
    public void shouldReturnResult() {
        lcs("ab", "cd").assertResult("abcd");
    }

    @Test(expected = AssertionError.class)
    public void shouldCheckErrorResult() {
        lcs("ab", "cd").assertResult("ERROR");
    }

    @Test(expected = AssertionError.class)
    public void shouldGetEmptyStrings() {
        lcs(null, "").assertResult((String)null);
    }

    @Override
    public LcsList getLcsAlgorithm() {
        return new LcsList() {
            @Override
            public <T> List<T> lcs(T[] xs, T[] ys) {
                List<T> list = new ArrayList<>(xs.length + ys.length);
                list.addAll(Arrays.asList(xs));
                list.addAll(Arrays.asList(ys));
                return list;
            }
        };
    }
}
