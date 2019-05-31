package sample;

import org.opencv.core.Scalar;

public class CvUtils {
    public static final Scalar COLOR_WHITE = colorRGB(255, 255, 255);
    /** Значение цвета */
    public static Scalar colorRGB(double red, double green, double blue) {
        return new Scalar(blue, green, red); }
}

