package com.nightcoder.mothercare.Supports;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Prefs {

    public static final String KEY_USERNAME = "USERNAME";
    public static final String USER_PREF = "UserPrefs";
    public static final String USER_TYPE = "userType";

    public static void putInt(Context context, String key, int val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREF, MODE_PRIVATE).edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public static void putBool(Context context, String key, boolean val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREF, MODE_PRIVATE).edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static void putLong(Context context, String key, long val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREF, MODE_PRIVATE).edit();
        editor.putLong(key, val);
        editor.apply();
    }

    public static void putFloat(Context context, String key, float val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREF, MODE_PRIVATE).edit();
        editor.putFloat(key, val);
        editor.apply();
    }

    public static void putString(Context context, String key, String val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREF, MODE_PRIVATE).edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static int getInt(Context context, String key, int defVal) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        return prefs.getInt(key, defVal);
    }

    public static boolean getBool(Context context, String key, boolean defVal) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        return prefs.getBoolean(key, defVal);
    }

    public static long getLong(Context context, String key, long defVal) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        return prefs.getLong(key, defVal);
    }

    public static float getFloat(Context context, String key, float defVal) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        return prefs.getFloat(key, defVal);
    }

    public static String getString(Context context, String key, String defVal) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        return prefs.getString(key, defVal);
    }
}
