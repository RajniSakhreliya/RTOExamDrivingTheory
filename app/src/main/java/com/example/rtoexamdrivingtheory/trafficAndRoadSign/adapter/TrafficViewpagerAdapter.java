package com.example.rtoexamdrivingtheory.trafficAndRoadSign.adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.rtoexamdrivingtheory.trafficAndRoadSign.FragmentTrafficSigns;

import java.util.ArrayList;

public class TrafficViewpagerAdapter extends FragmentStatePagerAdapter {

    private final Activity activity;
    private ArrayList<String> list = new ArrayList<>();
    private String TAG = "TrafficViewPager";

    public TrafficViewpagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (list.size() != 0) {
            return new FragmentTrafficSigns(list.get(position));
        }
        return null;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}