package com.example.rtoexamdrivingtheory.hazardTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.Utils;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.rey.material.widget.LinearLayout;

public class HazardCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private LinearLayout mRlt1;
    private LinearLayout mRlt2;
    private LinearLayout mRlt3;
    private LinearLayout mRlt4;
    private LinearLayout mRlt5;
    private LinearLayout mRlt6;
    private LinearLayout mRlt7;
    private LinearLayout mRlt8;
    private LinearLayout mRlt9;
    private LinearLayout mRlt10;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_category);
        activity = HazardCategoryActivity.this;
        WindowUtils.setStatusBarLight(activity, R.color.grey10);

        initView();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.ivBack);
        mIvBack.setOnClickListener(this);

        mRlt1 = (LinearLayout) findViewById(R.id.rlt1);
        mRlt1.setOnClickListener(this);

        mRlt2 = (LinearLayout) findViewById(R.id.rlt2);
        mRlt2.setOnClickListener(this);

        mRlt3 = (LinearLayout) findViewById(R.id.rlt3);
        mRlt3.setOnClickListener(this);

        mRlt4 = (LinearLayout) findViewById(R.id.rlt4);
        mRlt4.setOnClickListener(this);

        mRlt5 = (LinearLayout) findViewById(R.id.rlt5);
        mRlt5.setOnClickListener(this);

        mRlt6 = (LinearLayout) findViewById(R.id.rlt6);
        mRlt6.setOnClickListener(this);

        mRlt7 = (LinearLayout) findViewById(R.id.rlt7);
        mRlt7.setOnClickListener(this);

        mRlt8 = (LinearLayout) findViewById(R.id.rlt8);
        mRlt8.setOnClickListener(this);

        mRlt9 = (LinearLayout) findViewById(R.id.rlt9);
        mRlt9.setOnClickListener(this);

        mRlt10 = (LinearLayout) findViewById(R.id.rlt10);
        mRlt10.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.rlt1) {
            gotoHazardTest(1);
        } else if (id == R.id.rlt2) {
            gotoHazardTest(2);
        } else if (id == R.id.rlt3) {
            gotoHazardTest(3);
        } else if (id == R.id.rlt4) {
            gotoHazardTest(4);
        } else if (id == R.id.rlt5) {
            gotoHazardTest(5);
        } else if (id == R.id.rlt6) {
            gotoHazardTest(6);
        } else if (id == R.id.rlt7) {
            gotoHazardTest(7);
        } else if (id == R.id.rlt8) {
            gotoHazardTest(8);
        } else if (id == R.id.rlt9) {
            gotoHazardTest(9);
        } else if (id == R.id.rlt10) {
            gotoHazardTest(10);
        }
    }

    public void gotoHazardTest(int position) {
        if (!Utils.checkOnlineStatus(activity)) {
            Toast.makeText(activity, getResources().getString(R.string.checkInternetConnection), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(activity, HazardTestClipActivity.class);
        intent.putExtra("index", position);
        startActivity(intent);
    }
}