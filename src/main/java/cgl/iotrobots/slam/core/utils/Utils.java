package cgl.iotrobots.slam.core.utils;

public class Utils {
    public static double theta(double t) {
        return Math.atan2(Math.sin(t), Math.cos(t));
    }
}
