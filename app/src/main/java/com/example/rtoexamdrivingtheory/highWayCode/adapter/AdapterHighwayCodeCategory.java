package com.example.rtoexamdrivingtheory.highWayCode.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtoexamdrivingtheory.Constant;
import com.example.rtoexamdrivingtheory.model.HighWayCategories;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.AssetsUtil;
import com.progress.circleprogress.DonutProgress;

import java.util.ArrayList;

public class AdapterHighwayCodeCategory extends RecyclerView.Adapter<AdapterHighwayCodeCategory.ViewHolder> {

    Activity activity;
    ArrayList<HighWayCategories> listOfCat = new ArrayList<>();
    private onItemClickListener listener;
    private String TAG = "AdapterHighwayCategory";
    private ArrayList<Integer> colorList;

    public AdapterHighwayCodeCategory(Activity activity) {
        this.activity = activity;
    }

    public void setCategoryList(ArrayList<HighWayCategories> list) {
        this.listOfCat = list;
        colorList = Constant.getColorList(list.size());
        notifyDataSetChanged();
    }

    public void setListener(onItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_highway_code_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HighWayCategories highWayCategories = listOfCat.get(position);

        holder.rlBgHead.setBackgroundColor(colorList.get(position));

        holder.iconCategory.setImageBitmap(AssetsUtil.getBitmapFromAssetFile(activity, "highway/categories/" + (position + 1) + ".webp"));

        holder.progress.setDonut_progress(String.valueOf(highWayCategories.getPercentComplete()));
        holder.progress.setText(String.valueOf(highWayCategories.getPercentComplete())+"%");
        holder.tvTitle.setText(highWayCategories.getName());

        holder.rltClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(listOfCat.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfCat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlBgHead;
        ImageView iconCategory;
        DonutProgress progress;
        TextView tvTitle;
        RelativeLayout rltClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rlBgHead = itemView.findViewById(R.id.bg_head);
            iconCategory = itemView.findViewById(R.id.iconCategory);
            progress = itemView.findViewById(R.id.progress1);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            rltClick = itemView.findViewById(R.id.rltClick);

        }
    }

    public interface onItemClickListener {
        void onItemClick(HighWayCategories highWayCategories);
    }
}
