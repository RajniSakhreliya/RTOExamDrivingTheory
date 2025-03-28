package com.example.rtoexamdrivingtheory.trafficAndRoadSign.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtoexamdrivingtheory.model.ModelTraffic;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.AssetsUtil;
import com.rey.material.widget.RelativeLayout;

import java.util.ArrayList;

public class AdapterSigns extends RecyclerView.Adapter<AdapterSigns.Viewholder> {

    private final Activity activity;
    private ArrayList<ModelTraffic> list = new ArrayList<>();
    private OnSignClick listener;

    public interface OnSignClick {
        void onItemClick(ModelTraffic modelTraffic);
    }

    public AdapterSigns(Activity activity) {
        this.activity = activity;
    }

    public void setList(ArrayList<ModelTraffic> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(OnSignClick onSignClick) {
        this.listener = onSignClick;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(activity).inflate(R.layout.item_sign, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.ivSign.setImageBitmap(AssetsUtil.getBitmapFromAssetFile(activity, "theory/sign/" + list.get(position).getSign() + ".webp"));
        holder.tvSign.setText(list.get(position).getName());
        holder.rltClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView ivSign;
        TextView tvSign;
        RelativeLayout rltClick;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ivSign = itemView.findViewById(R.id.ivSign);
            tvSign = itemView.findViewById(R.id.tvSignDesc);
            rltClick = itemView.findViewById(R.id.rltClick);
        }
    }

}
