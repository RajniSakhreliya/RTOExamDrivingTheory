package com.example.rtoexamdrivingtheory.highWayCode.adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.rtoexamdrivingtheory.highWayCode.FragmentHighwayDetail;
import com.example.rtoexamdrivingtheory.model.HighWayItemModel;

import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {

    private final Activity activity;
    ArrayList<HighWayItemModel> list = new ArrayList<>();

    public ViewpagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
    }

    public void setList(ArrayList<HighWayItemModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (list.size() != 0) {
            return new FragmentHighwayDetail(list.get(position));
        }
        return null;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}