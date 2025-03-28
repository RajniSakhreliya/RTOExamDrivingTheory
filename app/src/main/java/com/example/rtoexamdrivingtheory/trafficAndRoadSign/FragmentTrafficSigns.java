package com.example.rtoexamdrivingtheory.trafficAndRoadSign;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtoexamdrivingtheory.model.ModelTraffic;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.trafficAndRoadSign.adapter.AdapterSigns;
import com.example.rtoexamdrivingtheory.MainApplication;

import java.util.ArrayList;

public class FragmentTrafficSigns extends Fragment implements AdapterSigns.OnSignClick {

    String category;
    private RecyclerView rcvTrafficSigns;
    AdapterSigns adapterSigns;
    private SqlLiteTrafficSigns sqlLiteTrafficSignHelper;
    ArrayList<ModelTraffic> listOfCat = new ArrayList<>();

    public FragmentTrafficSigns(String category) {
        this.category = category;
        sqlLiteTrafficSignHelper = MainApplication.getSqlLiteTrafficSigns();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traffic_signs, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapterSigns = new AdapterSigns(getActivity());
        adapterSigns.setListener(this);
        rcvTrafficSigns.setAdapter(adapterSigns);

        getSignListFromCategory();
    }

    String TAG = "FragmentTrafficSign";

    private void getSignListFromCategory() {
        listOfCat = sqlLiteTrafficSignHelper.getTrafficSignFromCategories(category);
        adapterSigns.setList(listOfCat);
        Log.e(TAG, "getSignListFromCategory: " + listOfCat.size());
    }

    private void initView(View view) {
        rcvTrafficSigns = view.findViewById(R.id.rcvSigns);
    }

    @Override
    public void onItemClick(ModelTraffic modelTraffic) {
        new DialogSign(getActivity(),modelTraffic).show();
    }
}
