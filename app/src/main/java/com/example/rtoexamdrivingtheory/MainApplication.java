package com.example.rtoexamdrivingtheory;

import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.example.rtoexamdrivingtheory.highWayCode.SqlLiteHighwayHelper;
import com.example.rtoexamdrivingtheory.trafficAndRoadSign.SqlLiteTrafficSigns;
import com.revisionquizmaker.drivingtheorytestrevision.SqlLiteAssetFeedHelper;

public class MainApplication extends MultiDexApplication {
    public SqlLiteAssetFeedHelper sqlLiteAssetFeedHelper;
    public static SqlLiteHighwayHelper sqlLiteHighwayHelper;
    public static SqlLiteTrafficSigns sqlLiteTrafficSigns;
    public static MainApplication mainApplication;

    public void onCreate() {
        super.onCreate();
        mainApplication = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        sqlLiteAssetFeedHelper = new SqlLiteAssetFeedHelper(getInstance());
        sqlLiteHighwayHelper = new SqlLiteHighwayHelper(getInstance());
        sqlLiteTrafficSigns = new SqlLiteTrafficSigns(getInstance());
    }

    public static SqlLiteHighwayHelper getSqlLiteHighwayHelper() {
        if (sqlLiteHighwayHelper == null) {
            sqlLiteHighwayHelper = new SqlLiteHighwayHelper(getInstance());
        }
        return sqlLiteHighwayHelper;
    }

    public static SqlLiteTrafficSigns getSqlLiteTrafficSigns() {
        if (sqlLiteTrafficSigns == null) {
            sqlLiteTrafficSigns = new SqlLiteTrafficSigns(getInstance());
        }
        return sqlLiteTrafficSigns;
    }

    private static Context getInstance() {
        if (mainApplication == null) {
            mainApplication = new MainApplication();
        }
        return mainApplication;
    }
}
