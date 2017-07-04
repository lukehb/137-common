package onethreeseven.common.util;

import java.awt.*;
import java.util.Random;

/**
 * A useful util - mostly used for generating colors.
 * Source mostly from: http://www.devx.com/tips/Tip/5515
 * @author Luke Bermingham
 */
public class ColorUtil {

    private ColorUtil() {
    }

    /**
     * Generates a series of colors such that the
     * distribution of the colors is (fairly) evenly spaced
     * throughout the color spectrum. This is especially
     * useful for generating unique color codes to be used
     * in a legend or on a graph.
     *
     * @param numColors the number of colors to generate
     * @return an array of Color objects representing the
     * colors in the table
     */
    public static Color[] generateNColors(int numColors) {
        Color[] table = new Color[numColors];

        if (numColors == 1) {
            // Special case for only one color
            table[0] = Color.red;
        } else {
            float hueMax = (float) 0.85;
            float sat = (float) 0.8;

            for (int i = 0; i < numColors; i++) {
                float hue = hueMax * i / (numColors - 1);

                // Here we interleave light colors and dark colors
                // to get a wider distribution of colors.
                if (i % 2 == 0) {
                    table[i] = Color.getHSBColor(hue, sat, (float) 0.9);
                    table[i] = new Color(table[i].getRed(), table[i].getGreen(), table[i].getBlue(), 255);
                } else {
                    table[i] = Color.getHSBColor(hue, sat, (float) 0.7);
                    table[i] = new Color(table[i].getRed(), table[i].getGreen(), table[i].getBlue(), 255);
                }
            }
        }

        return table;
    }

    /**
     * @param min   min value in regard to value
     * @param max   max value in regard to value
     * @param value the value passed in
     * @return a heat mapped color (blue equates to minimum values and red equates to maximum values).
     */
    public static Color generateHeatMapColor(double min, double max, double value) {
        assert value >= min && value <= max;
        if(value < min || value > max){
            throw new IllegalArgumentException("Value must be between min and max values, it was not.");
        }
        double power = (value - min) / (max - min); //will be between 0 and 1
        double H = Math.abs(0.75 - power * 0.75); //the color on hue chart
        double S = 0.6;
        double B = 0.9; // Brightness
        return Color.getHSBColor((float) H, (float) S, (float) B);
    }

    /**
     * @param min   min value in regard to value
     * @param max   max value in regard to value
     * @param value the value passed in
     * @return a fire mapped (white, yellow, orange, red, black) color
     */
    public static Color generateFireMapColor(double min, double max, double value) {
        assert value >= min && value <= max;
        if(value < min || value > max){
            throw new IllegalArgumentException("Value must be between min and max values, it was not.");
        }
        double power = (value - min) / (max - min); //will be between 0 and 1
        double H = Math.abs(0.138 - power * 0.138); //red-yellow
        double S = 1;
        double B = Math.abs(0.5 - power * 0.5) + 0.5; // Brightness
        return Color.getHSBColor((float) H, (float) S, (float) B);
    }

    public static Color generateGrayScaleColor(double min, double max, double value){
        assert value >= min && value <= max;
        if(value < min || value > max){
            throw new IllegalArgumentException("Value must be between min and max values, it was not.");
        }
        double power = (value - min) / (max - min); //will be between 0 and 1
        return Color.getHSBColor(0, 0, 1 - (float) power);
    }

    private static Random rand = new Random(137);

    /**
     * Using the heat map color function we generate random heat mapped colors
     * using the same random seed. Note: this does not guarantee uniqueness.
     * @return a heat mapped color.
     */
    public static Color nextColor(){
        return generateHeatMapColor(0, 1, rand.nextDouble());
    }

    public static String toHex(Color color) {
        String alpha = pad(Integer.toHexString(color.getAlpha()));
        String red = pad(Integer.toHexString(color.getRed()));
        String green = pad(Integer.toHexString(color.getGreen()));
        String blue = pad(Integer.toHexString(color.getBlue()));
        return "#" + red + green + blue + alpha;
        //return Integer.parseInt(hex, 16);
    }

    private static String pad(String s) {
        return (s.length() == 1) ? "0" + s : s;
    }

}
