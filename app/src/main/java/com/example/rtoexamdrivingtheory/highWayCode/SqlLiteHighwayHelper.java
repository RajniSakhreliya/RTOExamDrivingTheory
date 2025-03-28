package com.example.rtoexamdrivingtheory.highWayCode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rtoexamdrivingtheory.model.HighWayCategories;
import com.example.rtoexamdrivingtheory.model.HighWayItemModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SqlLiteHighwayHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "theory_highway.db";
    private static final String DB_PATH_SUFFIX = "databases";

    public SqlLiteHighwayHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        openDataBase(context);
    }

    public ArrayList<HighWayCategories> getHighWayCategories() {
        String TAG = "getHighwayCategories";
        ArrayList<HighWayCategories> catList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM categories order by _id asc;", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                HighWayCategories highWayCategories = new HighWayCategories();
                highWayCategories.setId(cursor.getInt(0));
                highWayCategories.setName(cursor.getString(1));
                highWayCategories.setRootCardID(cursor.getInt(2));
                highWayCategories.setCurrentIndex(cursor.getInt(3));
                highWayCategories.setTotalCards(cursor.getInt(4));

                catList.add(highWayCategories);
            }

            try {
                cursor.close();
            } catch (Exception e) {
                Log.e(TAG, "getHighWayCategories: ", e);
            }
            try {
                db.close();
            } catch (Exception e) {
                Log.e(TAG, "getHighWayCategories: ", e);
            }
            for (int i = 0; i < catList.size(); i++) {
                catList.get(i).setPercentComplete((getCategoryPercent(catList.get(i)) * 100) / catList.get(i).getTotalCards());
            }
        }
        return catList;
    }

    public int getCategoryPercent(HighWayCategories highWayCategories) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select count(*) from cards WHERE is_seen = 1 AND full_html IS NOT NULL AND full_html != \"\" and category_root_card_id = " + highWayCategories.getRootCardID() + ";", null);
        int i = 0;
        if (rawQuery != null && rawQuery.moveToFirst()) {
            i = rawQuery.getInt(0);
        }
        rawQuery.close();
        readableDatabase.close();
        return i;
    }

    public long setCurrentIndexOfCategory(HighWayCategories highWayCategories, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("curent_index", Integer.valueOf(i));
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int update = writableDatabase.update("categories", contentValues, "_id=?", new String[]{String.valueOf(highWayCategories.getId())});
        try {
            writableDatabase.close();
        } catch (Exception unused) {
        }
        return (long) update;
    }

    public HighWayCategories getHighwayCatRandom() {
        HighWayCategories highWayCategories = new HighWayCategories();
        SQLiteDatabase writableDatabase = getReadableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from categories WHERE _id > 1 and _id < 22 ORDER BY RANDOM() LIMIT 1;", null);
        if (rawQuery.moveToFirst()) {
            highWayCategories = new HighWayCategories();
            highWayCategories.setId(rawQuery.getInt(0));
            highWayCategories.setName(rawQuery.getString(1));
            highWayCategories.setRootCardID(rawQuery.getInt(2));
            highWayCategories.setCurrentIndex(rawQuery.getInt(3));
            highWayCategories.setTotalCards(rawQuery.getInt(4));
        }
        rawQuery.close();
        writableDatabase.close();
        highWayCategories.setPercentComplete((getCategoryPercent(highWayCategories) * 100) / highWayCategories.getTotalCards());
        return highWayCategories;
    }

    public HighWayItemModel getRandomHighWayRule(HighWayCategories highWayCategories) {
        HighWayItemModel highWayItemModel = new HighWayItemModel();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from cards WHERE full_html IS NOT NULL AND full_html != \"\" and category_root_card_id = " + highWayCategories.getRootCardID() + " ORDER BY RANDOM() LIMIT 1;;", null);
        if (rawQuery.moveToFirst()) {
            highWayItemModel = new HighWayItemModel();
            highWayItemModel.set_id(rawQuery.getInt(0));
            highWayItemModel.setDirect_parent_id(rawQuery.getInt(1));
            highWayItemModel.setCategory_root_card_id(rawQuery.getInt(2));
            highWayItemModel.setIndex_within_category(rawQuery.getInt(3));
            highWayItemModel.setIs_seen(rawQuery.getInt(4));
            highWayItemModel.setIs_flagged(rawQuery.getInt(5));
            highWayItemModel.setUrl(rawQuery.getString(6));
            highWayItemModel.setLong_title(rawQuery.getString(7));
            highWayItemModel.setShort_title(rawQuery.getString(8));
            highWayItemModel.setFull_html(rawQuery.getString(9));
            highWayItemModel.setSearchable_content(rawQuery.getString(10));
        }
        rawQuery.close();
        try {
            readableDatabase.close();
        } catch (Exception unused) {
        }
        return highWayItemModel;
    }

    public ArrayList<HighWayItemModel> getHighwayCodesByCategory(HighWayCategories highWayCategories) {
        ArrayList<HighWayItemModel> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = getReadableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("select * from cards WHERE full_html IS NOT NULL AND full_html != \"\" and category_root_card_id = " + highWayCategories.getRootCardID() + ";", null);
        if (rawQuery.moveToFirst()) {
            do {
                HighWayItemModel highWayItemModel = new HighWayItemModel();
                highWayItemModel.set_id(rawQuery.getInt(0));
                highWayItemModel.setDirect_parent_id(rawQuery.getInt(1));
                highWayItemModel.setCategory_root_card_id(rawQuery.getInt(2));
                highWayItemModel.setIndex_within_category(rawQuery.getInt(3));
                highWayItemModel.setIs_seen(rawQuery.getInt(4));
                highWayItemModel.setIs_flagged(rawQuery.getInt(5));
                highWayItemModel.setUrl(rawQuery.getString(6));
                highWayItemModel.setLong_title(rawQuery.getString(7));
                highWayItemModel.setShort_title(rawQuery.getString(8));
                highWayItemModel.setFull_html(rawQuery.getString(9));
                highWayItemModel.setSearchable_content(rawQuery.getString(10));
                arrayList.add(highWayItemModel);
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        writableDatabase.close();
        return arrayList;
    }

    public long setSeenHighwayCatItem(HighWayItemModel highwayCatItem) {
        highwayCatItem.setIs_seen(1);
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_seen", Integer.valueOf(highwayCatItem.getIs_seen()));
        SQLiteDatabase writableDatabase = getReadableDatabase();
        int update = writableDatabase.update("cards", contentValues, "_id=?", new String[]{String.valueOf(highwayCatItem.get_id())});
        try {
            writableDatabase.close();
        } catch (Exception unused) {
        }
        return (long) update;
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
            } catch (Exception e) {
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