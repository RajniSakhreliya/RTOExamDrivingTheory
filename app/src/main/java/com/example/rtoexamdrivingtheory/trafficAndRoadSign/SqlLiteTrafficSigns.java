package com.example.rtoexamdrivingtheory.trafficAndRoadSign;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rtoexamdrivingtheory.model.ModelTraffic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class SqlLiteTrafficSigns extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "theory.db";
    private static final String DB_PATH_SUFFIX = "databases";

    public SqlLiteTrafficSigns(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        openDataBase(context);
    }

    public ArrayList<String> getTrafficSignCategories() {
        String TAG = "getTrafficSignCategory";
        ArrayList<String> catList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ZSIGNS", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String type = cursor.getString(0);
                if (!catList.contains(type)) {
                    catList.add(type);
                }
            }
            try {
                cursor.close();
            } catch (Exception e) {
                Log.e(TAG, "getTrafficSignals : ", e);
            }
            try {
                db.close();
            } catch (Exception e) {
                Log.e(TAG, "getTrafficSignals : ", e);
            }
        }
        Collections.shuffle(catList);
        return catList;
    }

    public ArrayList<ModelTraffic> getTrafficSignFromCategories(String categoryType) {
        String TAG = "getTrafficSign";
        ArrayList<ModelTraffic> catList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ZSIGNS WHERE ZTYPE='" + categoryType + "'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ModelTraffic modelTraffic = new ModelTraffic();
                modelTraffic.setType(cursor.getString(0));
                modelTraffic.setName(cursor.getString(1));
                modelTraffic.setSign(cursor.getString(2));
                catList.add(modelTraffic);
            }
            try {
                cursor.close();
            } catch (Exception e) {
                Log.e(TAG, "getTrafficSignals : ", e);
            }
            try {
                db.close();
            } catch (Exception e) {
                Log.e(TAG, "getTrafficSignals : ", e);
            }
        }
        return catList;
    }

    public void CopyDataBaseFromAsset(Context ctx) throws IOException {
        InputStream myInput = ctx.getAssets().open(DB_PATH_SUFFIX + File.separator + DATABASE_NAME);
        String outFileName = getDatabasePath(ctx);

        File f = new File(ctx.getApplicationInfo().dataDir + File.separator + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdirs();

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private static String getDatabasePath(Context ctx) {
        return ctx.getApplicationInfo().dataDir + File.separator + DB_PATH_SUFFIX + File.separator + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase(Context context) throws SQLException {
        String TAG = "OpenDatabase";
        File dbFile = new File(getDatabasePath(context));
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset(context);
            } catch (IOException e) {
                Log.e(TAG, "openDataBase: ", e);
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}