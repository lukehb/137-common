package onethreeseven.common.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing pairing and un-pairing function.
 * @author Luke Bermingham
 */
public class PairingUtilTest {

    @Test
    public void pairLargeInts() throws Exception {

        int loops = 10000;

        for (int i = Integer.MAX_VALUE; i >= Integer.MAX_VALUE-loops; i--) {
            for (int j = Integer.MAX_VALUE; j >= Integer.MAX_VALUE-loops; j--) {
                long pairing = PairingUtil.from2dto1d(i,j);
                int[] res = PairingUtil.from1dto2d(pairing);
                Assert.assertEquals(i, res[0]);
                Assert.assertEquals(j, res[1]);
            }
        }

    }
}