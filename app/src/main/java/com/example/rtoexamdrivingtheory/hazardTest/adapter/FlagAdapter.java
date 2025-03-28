package com.example.rtoexamdrivingtheory.hazardTest.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtoexamdrivingtheory.model.FlagModel;
import com.example.rtoexamdrivingtheory.R;

import java.util.ArrayList;

public class FlagAdapter extends RecyclerView.Adapter<FlagAdapter.Viewholder> {

    private final Activity activity;
    ArrayList<FlagModel> listOfFlags = new ArrayList<>();

    public FlagAdapter(Activity activity) {
        this.activity = activity;
        listOfFlags.clear();
    }

    public void addFlag(FlagModel flagModel) {
        listOfFlags.add(flagModel);
        notifyDataSetChanged();
    }

    public void clearFlags() {
        listOfFlags.clear();
        notifyDataSetChanged();
    }

    public ArrayList<FlagModel> getAllFlags() {
        return listOfFlags;
    }

    public int getAllFlagCount() {
        return listOfFlags.size();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(activity).inflate(R.layout.item_flag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.ivFlag.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_flag));
    }

    @Override
    public int getItemCount() {
        return listOfFlags.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView ivFlag;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ivFlag = itemView.findViewById(R.id.ivFlag);
        }
    }
}
