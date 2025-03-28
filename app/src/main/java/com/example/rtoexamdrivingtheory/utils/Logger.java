package com.example.rtoexamdrivingtheory.utils;

import android.util.Log;

import androidx.annotation.Nullable;

public class Logger {
    public static final boolean DEBUGMODE = true;

    public static void v(@Nullable String tag, String msg) {
        if (DEBUGMODE) {
            Log.v(tag, msg);
        }
    }

    public static void d(@Nullable String tag, String msg) {
        if (DEBUGMODE) {
            Log.d(tag, msg);
        }
    }

    public static void i(@Nullable String tag, String msg) {
        if (DEBUGMODE) {
            Log.i(tag, msg);
        }
    }

    public static void w(@Nullable String tag, String msg) {
        if (DEBUGMODE) {
            Log.w(tag, msg);
        }
    }

    public static void e(@Nullable String tag, String msg) {
        if (DEBUGMODE) {
            Log.e(tag, msg);
        }
    }

    public static void v(@Nullable String tag, String msg, Exception e) {
        if (DEBUGMODE) {
            Log.v(tag, msg, e);
        }
    }

    public static void d(@Nullable String tag, String msg, Exception e) {
        if (DEBUGMODE) {
            Log.d(tag, msg, e);
        }
    }

    public static void i(@Nullable String tag, String msg, Exception e) {
        if (DEBUGMODE) {
            Log.i(tag, msg, e);
        }
    }

    public static void w(@Nullable String tag, String msg, Exception e) {
        if (DEBUGMODE) {
            Log.w(tag, msg, e);
        }
    }

    public static void e(@Nullable String tag, String msg, Exception e) {
        if (DEBUGMODE) {
            Log.e(tag, msg, e);
        }
    }
}
