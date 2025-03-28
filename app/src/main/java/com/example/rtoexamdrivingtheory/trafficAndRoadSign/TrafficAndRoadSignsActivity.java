package com.example.rtoexamdrivingtheory.trafficAndRoadSign;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.trafficAndRoadSign.adapter.TrafficViewpagerAdapter;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.google.android.material.tabs.TabLayout;
import com.example.rtoexamdrivingtheory.MainApplication;

import java.util.ArrayList;

public class TrafficAndRoadSignsActivity extends AppCompatActivity {

    private Activity activity;
    private SqlLiteTrafficSigns sqlLiteTrafficSignHelper;
    private ImageView mIvBack;
    private ViewPager mViewpager;
    TrafficViewpagerAdapter trafficViewpagerAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_and_road_signs);

        activity = TrafficAndRoadSignsActivity.this;

        WindowUtils.setStatusBarLight(activity, R.color.grey10);

        sqlLiteTrafficSignHelper = MainApplication.getSqlLiteTrafficSigns();

        initView();

        trafficViewpagerAdapter = new TrafficViewpagerAdapter(getSupportFragmentManager(), activity);

        mViewpager.setAdapter(trafficViewpagerAdapter);

        mViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewpager));
        getList();
    }

    private void getList() {
        ArrayList<String> listOfSignCategory = sqlLiteTrafficSignHelper.getTrafficSignCategories();
        trafficViewpagerAdapter.setList(listOfSignCategory);

        for (int i = 0; i < listOfSignCategory.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(listOfSignCategory.get(i)));
        }
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.ivBack);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout =  findViewById(R.id.tabs);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}