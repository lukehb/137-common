package onethreeseven.common.util;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A utility class useful for working with n-dimensional data (and their indices).
 * @author Luke Bermingham.
 */
public final class NDUtil {

    private NDUtil() {
    }

    /**
     * Given n-dimensional indices and their dimensional extents flatten them into a 1d index.
     * More detail: http://stackoverflow.com/questions/7367770/how-to-flatten-or-index-3d-array-in-1d-array
     *
     * @param indices n-dimensional indices
     * @param extents the maximum size in each dimension
     * @return Converts n-dimensional indices into a 1-dimension index
     */
    public static int flattenIndices(int[] indices, int[] extents) {
        int totalIdx = 0;
        for (int i = 0; i < indices.length; i++) {
            int idx = indices[i];
            for (int j = 0; j < i; j++) {
                int extent = extents[j];
                idx *= extent;
            }
            totalIdx += idx;
        }
        return totalIdx;
    }

    /**
     * Given a 1d index inflate it to n-dimensions using n-dimensional extents.
     * @param totalIdx the 1d index
     * @param extents  the n-dimensional extents
     * @return n-dimensional indices
     */
    public static int[] inflateIndex(int totalIdx, int[] extents) {
        int nDimensions = extents.length;
        int[] indices = new int[nDimensions];

        for (int i = 0; i < nDimensions; ++i) {
            indices[i] = totalIdx;
            for (int j = 0; j < i; ++j) {
                indices[i] /= extents[j];
            }
            indices[i] %= extents[i];
        }
        return indices;
    }

    /**
     * Given a multiple n-d indices flatten them to a collection of 1d indices
     *
     * @param multipleIndices multiple n-d indices
     * @param extents         the maximum size in each dimension
     * @return the collection of 1d indices
     */
    public static int[] flattenIndicesCollection(int[][] multipleIndices, int[] extents) {
        int[] flattened = new int[multipleIndices.length];
        for (int i = 0; i < multipleIndices.length; i++) {
            flattened[i] = flattenIndices(multipleIndices[i], extents);
        }
        return flattened;
    }

    /**
     * Given a set of n-d indices find their shape (dimensions, ie. width x height x depth)
     * @param bounds n-dimensional bounds like { {min,max}, {min,max} .... etc }
     * @return the n-dimensional shape
     */
    public static int[] getShape(int[][] bounds) {
        int nDimensions = bounds.length;
        int[] shape = new int[nDimensions];
        //get shape
        for (int n = 0; n < nDimensions; n++) {
            shape[n] = bounds[n][1] = bounds[n][0];
        }
        return shape;
    }

    /**
     * Given a start and end point in n-d space,
     * find all the integer points on a straight line between them.
     * Similar to Bresenham's line algorithm: http://www.cb.uu.se/~cris/blog/index.php/archives/400
     * @param start start point
     * @param end   end point
     * @return a sequence of points resolve start to end in integer steps
     */
    public static int[][] interpolate(int[] start, int[] end) {
        //idea is we find the largest dimensional gap between the two point

        //end[] minus start[]
        double[] dimStep = IntStream.range(0, start.length).map(i -> end[i] - start[i]).asDoubleStream().toArray();

        //find the max absolute element in the dimStep
        int nSteps = Maths.maxAbsElement(dimStep);
        //once we have the largest gap, we figure out the step width per dimension
        dimStep = Arrays.stream(dimStep).map(x -> x/nSteps).toArray();

        int[][] points = new int[nSteps + 1][start.length];
        //store first value
        points[0] = start;

        //make a current array that will be updated in the iterator
        double[] cur = Arrays.stream(start).asDoubleStream().toArray();

        for (int i = 1; i < nSteps; i++) {
            //add s to cur
            for (int j = 0; j < cur.length; j++) {
                cur[j] += dimStep[j];
            }
            //round the result in cur and add that to our output
            points[i] = Arrays.stream(cur).mapToInt(x -> (int)Math.round(x)).toArray();
        }
        points[nSteps] = end;
        return points;
    }

    /**
     * Translate a set of n-d points using some specified n-d offset
     * @param pts    n-d points
     * @param offset n-d offset
     */
    public static void translatePts(double[][] pts, double[] offset) {
        assert pts[0].length == offset.length;
        for (double[] pt : pts) {
            for (int n = 0; n < pt.length; n++) {
                pt[n] += offset[n];
            }
        }
    }

    /**
     * Copies some n-d points.
     * @param ndPoints the points to copy
     * @return the copied n-d points
     */
    public static double[][] copyPts(double[][] ndPoints) {
        double[][] copy = new double[ndPoints.length][];
        for (int i = 0; i < ndPoints.length; i++) {
            copy[i] = new double[ndPoints[i].length];
            System.arraycopy(ndPoints[i], 0, copy[i], 0, copy[i].length);
        }
        return copy;
    }

    /**
     * Find the average n-d point in a collection of n-d points.
     * @param ndPoints a bunch of n-d points
     * @return the average nd point. Note this is just the actual average,
     * it may not be a real point resolve the input ndPoints.
     */
    public static double[] averagePt(double[][] ndPoints) {
        int nDimensions = ndPoints[0].length;
        double[] avg = new double[nDimensions];
        for (double[] ndPoint : ndPoints) {
            for (int n = 0; n < nDimensions; n++) {
                avg[n] += ndPoint[n];
            }
        }
        for (int n = 0; n < nDimensions; n++) {
            avg[n] /= ndPoints.length;
        }
        return avg;
    }

    public static double[][] toDoubles(int[][] intPts) {
        int nDimensions = intPts[0].length;
        double[][] pts = new double[intPts.length][nDimensions];
        for (int i = 0; i < intPts.length; i++) {
            for (int n = 0; n < nDimensions; n++) {
                pts[i][n] = ((Integer) intPts[i][n]).doubleValue();
            }
        }
        return pts;
    }

    /**
     * @param nDimensions how many dimensions should the result have
     * @param max Generate number between [0, max)
     * @return the n-dimension point.
     */
    public static int[] generateNdPt(int nDimensions, int max){
        Random r = new Random();
        int[] result = new int[nDimensions];
        for (int i = 0; i < nDimensions; i++) {
            result[i] = r.nextInt(max);
        }
        return result;
    }


}
