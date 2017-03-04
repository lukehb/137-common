package onethreeseven.common.util;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

/**
 * Testing the conversion of 1d and n-d indices.
 * @see NDUtil
 * @author Luke Bermingham
 */
public class NDUtilTest {

    @Test
    public void testFlattenIndices() throws Exception {

        //test inflation on a 3x3x3 cube

        int flattened = NDUtil.flattenIndices(new int[]{2, 2, 2}, new int[]{3, 3, 3});
        Assert.assertTrue(flattened == 26);

        int flattenUneven = NDUtil.flattenIndices(new int[]{0, 1, 2}, new int[]{3, 3, 3});
        Assert.assertTrue(flattenUneven == 21);

        int flattenNonUniform = NDUtil.flattenIndices(new int[]{1, 3, 7}, new int[]{3, 6, 9});
        Assert.assertTrue(flattenNonUniform == 136);
    }


    @Test
    public void testInterpolate() throws Exception {
        int[] start = NDUtil.generateNdPt(4, 10);
        int[] end = NDUtil.generateNdPt(4, 10);

        int[][] inbetween = NDUtil.interpolate(start,end);

        for (int i = 0; i < inbetween.length; i++) {
            int[] cur = inbetween[i];
            System.out.println(Arrays.toString(cur));
            if (i != inbetween.length - 1) {
                int[] sub = Maths.sub(cur, inbetween[i+1]);
                int maxAbsElem = Maths.maxAbsElement(Arrays.stream(sub).asDoubleStream().toArray());
                //the difference between this point and next must be at most one cell index
                Assert.assertTrue(maxAbsElem <= 1);
            }
        }
    }

    @Test
    public void testInflateIndex() throws Exception {

        int[] extents = new int[]{3, 6, 9};

        int[] indices = new int[]{1, 3, 7};

        int[] result = NDUtil.inflateIndex(136, extents);
        Assert.assertTrue(Arrays.equals(result, indices));
    }
}