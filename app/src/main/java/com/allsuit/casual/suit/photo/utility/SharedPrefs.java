package com.allsuit.casual.suit.photo.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    public static final String AccessToken = "AccessToken";
    public static final String BACKGROUND_CAMERA = "camera_file";
    public static final String BACKGROUND_IMAGE = "background_image";
    public static final String DEVICE_ID = "device_id";
    public static final String ERASER_OFFSET = "eraser_offset";
    public static final String ERASER_SIZE = "eraser_size";
    public static final String IS_APP_RATED = "is_rate";
    public static final String ITEM_SIZE = "item_size";
    public static final String ProfileId = "ProfileId";
    public static final String REPAIR_SIZE = "repair_size";
    private static String SHARED_PREFS_FILE_NAME = "sareesuit_shared_prefs";
    public static final String SHOWFACE = "showface";
    public static final String SHOW_RATE_DLG = "show_rate";
    public static final String SHOW_S1 = "show_s1";
    public static final String SHOW_S2 = "show_s2";
    public static final String SHOW_S3 = "show_s3";
    public static final String SPLASH_AD_DATA = "Ad_data";
    public static final String SUITJSONDATA = "suitjsondata";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, 0);
    }

    public static boolean contain(Context context, String key) {
        return getPrefs(context).contains(key);
    }

    public static void clearPrefs(Context context) {
        String device_id = getString(context, DEVICE_ID);
        getPrefs(context).edit().clear().commit();
        save(context, DEVICE_ID, device_id);
    }

    public static void savePref(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    public static void save(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }

    public static void save(Context context, String key, float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0.0f);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPrefs(context).getFloat(key, defaultValue);
    }

    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }

    public static void removeKey(Context context, String key) {
        getPrefs(context).edit().remove(key).commit();
    }
}
