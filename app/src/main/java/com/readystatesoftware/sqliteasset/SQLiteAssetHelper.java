package com.readystatesoftware.sqliteasset;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class SQLiteAssetHelper extends SQLiteOpenHelper {
    private static final String a = "SQLiteAssetHelper";
    private final Context context;
    private final String dbName;
    private final CursorFactory cursorFactory;
    private final int e;
    private SQLiteDatabase f;
    private boolean g;
    private final String h;
    private final String i;
    private final String j;
    private final int k;

    public static class SQLiteAssetException extends SQLiteException {
        public SQLiteAssetException(String str) {
            super(str);
        }
    }

    public final void onConfigure(SQLiteDatabase sQLiteDatabase) {
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public final void onDowngrade(SQLiteDatabase sQLiteDatabase, int i2, int i3) {
    }

    public SQLiteAssetHelper(Context context, String dbName, String str2, CursorFactory cursorFactory, int i2) {
        super(context, dbName, cursorFactory, i2);
        this.f = null;
        this.g = false;
        this.k = 0;
        if (i2 < 1) {
            String sb = "Version must be >= 1, was " +
                    i2;
            throw new IllegalArgumentException(sb);
        } else if (dbName != null) {
            this.context = context;
            this.dbName = dbName;
            this.cursorFactory = cursorFactory;
            this.e = i2;
            this.i = "databases/" +
                    dbName;
            if (str2 != null) {
                this.h = str2;
            } else {
                this.h = context.getApplicationInfo().dataDir +
                        "/databases";
            }
            this.j = "databases/" +
                    dbName +
                    "_upgrade_%s-%s.sql";
        } else {
            throw new IllegalArgumentException("Database name cannot be null");
        }
    }

    public SQLiteAssetHelper(Context context, String dbName, CursorFactory cursorFactory, int i2) {
        this(context, dbName, null, cursorFactory, i2);
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sQLiteDatabase;
        if (this.f != null && this.f.isOpen() && !this.f.isReadOnly()) {
            return this.f;
        } else if (!this.g) {
            SQLiteDatabase sQLiteDatabase2 = null;
            try {
                this.g = true;
                sQLiteDatabase = a(false);
                try {
                    int version = sQLiteDatabase.getVersion();
                    if (version != 0 && version < this.k) {
                        sQLiteDatabase2 = a(true);
                        sQLiteDatabase2.setVersion(this.e);
                        sQLiteDatabase = sQLiteDatabase2;
                        version = sQLiteDatabase2.getVersion();
                    }
                    if (version != this.e) {
                        sQLiteDatabase.beginTransaction();
                        if (version == 0) {
                            onCreate(sQLiteDatabase);
                        } else {
                            onUpgrade(sQLiteDatabase, version, this.e);
                        }
                        sQLiteDatabase.setVersion(this.e);
                        sQLiteDatabase.setTransactionSuccessful();
                        sQLiteDatabase.endTransaction();
                    }
                    onOpen(sQLiteDatabase);
                    this.g = false;
                    if (this.f != null) {
                        this.f.close();
                    }
                    this.f = sQLiteDatabase;
                    return sQLiteDatabase;
                } catch (Throwable th) {
                    this.g = false;
                    throw th;
                }
            } catch (Throwable th2) {
                sQLiteDatabase = sQLiteDatabase2;
                this.g = false;
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
            }
        } else {
            throw new IllegalStateException("getWritableDatabase called recursively");
        }
        return sQLiteDatabase;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase sQLiteDatabase = null;
        if (this.f != null && this.f.isOpen()) {
            return this.f;
        } else if (!this.g) {
            try {
                return getWritableDatabase();
            } catch (SQLiteException e2) {
                if (this.dbName != null) {
                    this.g = true;
                    String path = this.context.getDatabasePath(this.dbName).getPath();
                    sQLiteDatabase = SQLiteDatabase.openDatabase(path, this.cursorFactory, SQLiteDatabase.OPEN_READONLY);
                    if (sQLiteDatabase.getVersion() == this.e) {
                        onOpen(sQLiteDatabase);
                        this.f = sQLiteDatabase;
                        SQLiteDatabase sQLiteDatabase2 = this.f;
                        this.g = false;
                        return sQLiteDatabase2;
                    }
                    String sb3 = "Can't upgrade read-only database from version " +
                            sQLiteDatabase.getVersion() +
                            " to " +
                            this.e +
                            ": " +
                            path;
                    throw new SQLiteException(sb3);
                }
                throw e2;
            } catch (Throwable th2) {
                this.g = false;
            }
        } else {
            throw new IllegalStateException("getReadableDatabase called recursively");
        }
        return sQLiteDatabase;
    }

    public synchronized void close() {
        if (this.g) {
            throw new IllegalStateException("Closed during initialization");
        } else if (this.f != null && this.f.isOpen()) {
            this.f.close();
            this.f = null;
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        a(i2, i3 - 1, i3, arrayList);
        if (!arrayList.isEmpty()) {
            Collections.sort(arrayList, new SqlComparator());
            for (Object o : arrayList) {
                String str2 = (String) o;
                try {
                    String b2 = SqlAssets.b(this.context.getAssets().open(str2));
                    if (b2 != null) {
                        for (String str4 : SqlAssets.list(b2, ';')) {
                            if (!str4.trim().isEmpty()) {
                                sQLiteDatabase.execSQL(str4);
                            }
                        }
                    }
                } catch (IOException ignored) {
                }
            }
            return;
        }
        String sb5 = "no upgrade script path from " +
                i2 +
                " to " +
                i3;
        throw new SQLiteAssetException(sb5);
    }

    private SQLiteDatabase a(boolean z) {
        String sb = this.h +
                "/" +
                this.dbName;
        SQLiteDatabase a2 = new File(sb).exists() ? a() : null;
        if (a2 != null) {
            if (z) {
                Log.w(a, "forcing database upgrade!");
                b();
                a2 = a();
            }
            return a2;
        }
        b();
        return a();
    }

    private SQLiteDatabase a() {
        try {
            String sb = this.h +
                    "/" +
                    this.dbName;
            return SQLiteDatabase.openDatabase(sb, this.cursorFactory, 0);
        } catch (SQLiteException e2) {
            return null;
        }
    }

    private void b() {
        String sb2 = this.h +
                "/" +
                this.dbName;
        try {
            InputStream inputStream = this.context.getAssets().open(this.i);
            String sb3 = this.h +
                    "/";
            File file = new File(sb3);
            if (!file.exists()) {
                file.mkdirs();
            }
            SqlAssets.getAsset(inputStream, Files.newOutputStream(Paths.get(sb2)));
        } catch (Exception e2) {
            String sb4 = "Unable to write " +
                    sb2 +
                    " to data directory";
            SQLiteAssetException sQLiteAssetException = new SQLiteAssetException(sb4);
            sQLiteAssetException.setStackTrace(e2.getStackTrace());
            throw sQLiteAssetException;
        }
    }

    private InputStream a(int i2, int i3) {
        String format = String.format(this.j, i2, i3);
        try {
            return this.context.getAssets().open(format);
        } catch (Exception unused) {
            return null;
        }
    }

    private void a(int i2, int i3, int i4, ArrayList<String> arrayList) {
        int i5;
        if (a(i3, i4) != null) {
            arrayList.add(String.format(this.j, i3, i4));
            i4 = i3;
        }
        i5 = i3 - 1;
        if (i5 >= i2) {
            a(i2, i5, i4, arrayList);
        }
    }
}
