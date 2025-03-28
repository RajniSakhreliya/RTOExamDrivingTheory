package com.example.rtoexamdrivingtheory.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rtoexamdrivingtheory.R;

public class PreferencesUtils {
    public static String PREF_NAME;

    private static PreferencesUtils preferencesUtils;
    private static Context mContext;
    private static String IS_PURCHASE = "is_purchase";

    public static PreferencesUtils getInstance(Context context) {
        mContext = context;
        PREF_NAME = context.getString(R.string.app_name);
        if (preferencesUtils == null)
            preferencesUtils = new PreferencesUtils();
        return preferencesUtils;
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean def) {
        if (mContext != null)
            return mContext.getSharedPreferences(PREF_NAME, 0).getBoolean(key, def);
        else
            return def;
    }

    public String getPrefString(String key) {
        if (mContext != null)
            return mContext.getSharedPreferences(PREF_NAME, 0).getString(key, "");
        else
            return "";
    }

    public void setPrefString(String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setPurchase(boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putBoolean(IS_PURCHASE, value);
        editor.commit();
    }

    public boolean checkPurchase() {
        if (mContext != null)
            return mContext.getSharedPreferences(PREF_NAME, 0).getBoolean(IS_PURCHASE, false);
        else
            return false;
    }
}
