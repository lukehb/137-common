package onethreeseven.common.util;


/**
 * Utility for pairing functions.
 * Pairing functions uniquely map f(x,y) → f(z) and the inverse.
 * @see <a href="https://en.wikipedia.org/wiki/Pairing_function">wikipedia article</a>
 * @see <a href="http://stackoverflow.com/questions/919612/mapping-two-integers-to-one-in-a-unique-and-deterministic-way">SO Post</a>
 * @author Luke Bermingham
 */
public final class PairingUtil {

    /**
     * Converts 2d coordinate to 1d using Szudzik's pairing function.
     * Note: no negatives allowed.
     * @param x The first coordinate.
     * @param y The second coordinate.
     * @return The 1d representation.
     */
    public static long from2dto1d(int x, int y){
        if(x < 0 || y < 0){
            throw new IllegalArgumentException("Values must both be positive.");
        }
        if (x >= y)
        {
            return (long)x * x + x + y;
        }
        else
        {
            return (long)y * y + x;
        }
    }

    /**
     * Converts a 1d coordinate its 2d mapping using the inverse of Szudzik's
     * pairing function. Note: no negatives allowed.
     * @param z The one dimensional coordinate to transform into two coordinates.
     * @return The 2d representation [0] = x and [1] = y.
     */
    public static int[] from1dto2d(long z){
        if(z < 0){
            throw new IllegalArgumentException("Value must be positive.");
        }
        int[] pair = new int[2];
        double preciseZ = Math.sqrt(z);
        long floor = (long)Math.floor(preciseZ);
        if (floor * floor > z)
        {
            floor--;
        }
        long t = z - (floor*floor);
        if (t < floor)
        {
            pair[0] = (int)t;
            pair[1] = (int)floor;
        }
        else
        {
            pair[0] = (int)floor;
            pair[1] = (int)t - (int)floor;
        }
        return pair;
    }

}
