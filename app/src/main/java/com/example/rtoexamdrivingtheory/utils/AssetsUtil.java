package com.example.rtoexamdrivingtheory.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class AssetsUtil {
    public static final String ENCODING = "UTF-8";

    private AssetsUtil() {
        throw new AssertionError();
    }

    public static InputStream getFileFromAssets(Context context, String fileName) throws IOException {
        AssetManager am = context.getAssets();
        return am.open(fileName);
    }

    public static String getTextFromAssets(Context context, String fileName) {
        String result = null;
        InputStream in = null;
        try {
            in = getFileFromAssets(context, fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = new String(buffer, Charset.forName(ENCODING));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap getBitmapFromAssetFile(Activity activity, String filePath) {
        String TAG = "BitmapFromAsset";
        Log.e(TAG, "getBitmapFromAssetFile: "+filePath );
        try {
            AssetManager assetManager = activity.getAssets();
            InputStream istr = null;
            istr = assetManager.open(filePath);
            return BitmapFactory.decodeStream(istr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String openRawResource(Context context, int file) {
        InputStream is = context.getResources().openRawResource(file);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        return jsonString;
    }
}
