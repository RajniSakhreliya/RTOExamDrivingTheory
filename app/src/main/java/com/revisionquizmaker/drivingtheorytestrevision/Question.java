package com.revisionquizmaker.drivingtheorytestrevision;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.revisionquizmaker.drivingtheorytestrevision.SqlBaseColumn.C0057a;
import com.example.rtoexamdrivingtheory.MainApplication;

public class Question {
    public static void copyDatabase(MainApplication mainApplication, AsyncTask asyncTask) {
    }

    public static void copyDatabase(SqlLiteAssetHelper cVar, SqlLiteAssetFeedHelper bVar) {
        SQLiteDatabase readableDatabase = cVar.getReadableDatabase();
        SQLiteDatabase writableDatabase = bVar.getWritableDatabase();
        Cursor query = readableDatabase.query("entry",
                new String[]{"_id", "category_percentage_correct"},
                null, null, null, null, null);
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex("_id"));
            String str = "question_corresponding_quiz=" +
                    string;
            Cursor query2 = readableDatabase.query("questionTable", C0057a.c, str, null, null, null, null);
            Cursor query3 = writableDatabase.query("questionTable", C0057a.c, str, null, null, null, null);
            while (query2.moveToNext()) {
                if (query3.moveToNext()) {
                    String string2 = query2.getString(query2.getColumnIndex("questionAnsweredCorrectly"));
                    String string3 = query3.getString(query3.getColumnIndex("_id"));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("questionAnsweredCorrectly", string2);
                    String[] strArr = {string3};
                    writableDatabase.update("questionTable", contentValues, "_id = ?", strArr);
                }
            }
            query2.close();
            query3.close();
            String sb3 = "question_corresponding_quiz=" +
                    string;
            copyDatabase(sb3, string, writableDatabase);
        }
        query.close();
        readableDatabase.close();
        writableDatabase.close();
    }

    public static Cursor createCursor(MainApplication mainApplication) {
        return mainApplication.sqlLiteAssetFeedHelper.getReadableDatabase().query("entry",
                new String[]{"_id", "title", "category_percentage_correct"},
                null, null, null, null, null);
    }

    public static Cursor questionCorrespondingQuiz(int i, MainApplication mainApplication) {
        SQLiteDatabase readableDatabase = mainApplication.sqlLiteAssetFeedHelper.getReadableDatabase();
        String sb = "question_corresponding_quiz=" + i;
        return readableDatabase.query("questionTable", C0057a.b, sb, null, null, null, null);
    }

    public static int questionCorrespondingCategorySize(int i, MainApplication mainApplication) {
        SQLiteDatabase readableDatabase = mainApplication.sqlLiteAssetFeedHelper.getReadableDatabase();
        String sb = "question_corresponding_quiz=" + i;
        Cursor query = readableDatabase.query("questionTable",
                C0057a.b, sb, null, null, null, null);
        return query.getCount();
    }

    public static Cursor answerCorrespondingQuestion(int i, MainApplication mainApplication) {
        SQLiteDatabase readableDatabase = mainApplication.sqlLiteAssetFeedHelper.getReadableDatabase();
        String sb = "answer_corresponding_question=" + i;
        return readableDatabase.query("answerTable", C0057a.d, sb, null, null, null, null);
    }

    public static void setAns(int i, int i2, MainApplication mainApplication, Boolean bool) {
        SQLiteDatabase writableDatabase = mainApplication.sqlLiteAssetFeedHelper.getWritableDatabase();
        String sb2 = "_id=" + i;
        ContentValues contentValues = new ContentValues();
        String str = "NO";
        if (bool) {
            str = "YES";
        }
        contentValues.put("questionAnsweredCorrectly", str);
        writableDatabase.update("questionTable", contentValues, sb2, null);
        String sb3 = "question_corresponding_quiz=" + i2;
        copyDatabase(sb3, String.valueOf(i2), writableDatabase);
    }

    private static void copyDatabase(String str, String str2, SQLiteDatabase sQLiteDatabase) {
        Cursor query = sQLiteDatabase.query("questionTable",
                new String[]{"_id", "questionAnsweredCorrectly", "question_corresponding_quiz"},
                str, null, null, null, null);
        double count = (double) query.getCount();
        query.moveToPosition(-1);
        double d = 0.0d;
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndexOrThrow("questionAnsweredCorrectly"));
            if (string != null && string.equals("YES")) {
                d += 1.0d;
            }
        }
        int i = (int) ((d / count) * 100.0d);
        ContentValues contentValues = new ContentValues();
        String sb2 = "_id=" + str2;
        contentValues.put("category_percentage_correct", Integer.toString(i));
        sQLiteDatabase.update("entry", contentValues, sb2, null);
        query.close();
    }
}
