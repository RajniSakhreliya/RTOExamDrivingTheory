package com.example.rtoexamdrivingtheory.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static String SHARED_PREFERENCE_ANSWERS = "AnswerSharedPreference";

    public static String getAlphabet(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : "";
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static boolean checkOnlineStatus(Activity activity) {
        @SuppressLint("WrongConstant")
        NetworkInfo netInfo = ((ConnectivityManager) activity.getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
