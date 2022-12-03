package com.allsuit.casual.suit.photo.utility;

import android.content.res.Resources;


public class DisplayMetricsHandler {
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static int getDensityDPI() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static int imageWidthCalc() {
        float density = Resources.getSystem().getDisplayMetrics().density;
        if (((double) density) == 0.75d) {
            return 43;
        }
        if (((double) density) == 1.0d) {
            return 57;
        }
        if (((double) density) == 1.5d) {
            return 86;
        }
        if (((double) density) == 2.0d) {
            return 150;
        }
        if (((double) density) == 3.0d) {
            return 195;
        }
        if (((double) density) != 4.0d) {
            return 100;
        }
        return 0;
    }

    public static String getDPI() {
        float density = Resources.getSystem().getDisplayMetrics().density;
        if (((double) density) == 0.75d) {
            return "LDPI";
        }
        if (((double) density) == 1.0d) {
            return "MDPI";
        }
        if (((double) density) == 1.5d) {
            return "HDPI";
        }
        if (((double) density) == 2.0d) {
            return "XHDPI";
        }
        if (((double) density) == 3.0d) {
            return "XXHDPI";
        }
        if (((double) density) == 4.0d) {
            return "XXXHDPI";
        }
        return "HDPI";
    }
}
