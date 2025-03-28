package com.example.rtoexamdrivingtheory.category.adapter;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtoexamdrivingtheory.model.ModelCategory;
import com.example.rtoexamdrivingtheory.Constant;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.AssetsUtil;
import com.example.rtoexamdrivingtheory.view.CustomProgress;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {
    private ArrayList<ModelCategory> catList = new ArrayList<>();
    Activity activity;
    private CatClickListener catClickListener;
    ArrayList<Integer> colorList = new ArrayList<>();

    public interface CatClickListener {
        void onCatClick(ModelCategory modelCategory);
    }

    public AdapterCategory(Activity activity) {
        this.activity = activity;
    }

    public void setList(ArrayList<ModelCategory> list) {
        this.catList = list;
        colorList = Constant.getColorList(list.size());
        notifyDataSetChanged();
    }

    public void setListener(CatClickListener catClickListener) {
        this.catClickListener = catClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.rlBgColor.setBackgroundColor(colorList.get(position));

        ModelCategory modelCategory = catList.get(position);

        String name = modelCategory.getCategoryName().toLowerCase();
        name = name.replace(" ", "_");
        holder.ivCatImage.setImageBitmap(AssetsUtil.getBitmapFromAssetFile(activity, "category/" + name + ".webp"));

        holder.tvCatTitle.setText(modelCategory.getCategoryName());
        holder.tvCatTitle.setSelected(true);

        int percentageQuestion = (modelCategory.getTotalQuestions() * 86) / 100;
        StringBuilder sb = new StringBuilder();
        sb.append(activity.getResources().getString(R.string.mustget86front)).append(" ");
        sb.append("(").append(percentageQuestion).append(" ").append(activity.getResources().getString(R.string.outOf)).append(" ").append(modelCategory.getTotalQuestions()).append(") ");
        sb.append(activity.getResources().getString(R.string.toPasstheTest));

        Spannable wordSpan = new SpannableString(sb);
        wordSpan.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.green_500)), 16, wordSpan.length() - 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 16, wordSpan.length() - 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvCatDetail.setText(wordSpan);

        String des = activity.getResources().getString(R.string.pass) + " " + modelCategory.getAnsYesQue() + "/" + modelCategory.getTotalQuestions();
        Spannable wordToSpanPassDes = new SpannableString(des);
        wordToSpanPassDes.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.green_500)), 5, des.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpanPassDes.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 5, des.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvPassDes.setText(wordToSpanPassDes);

        holder.customProgress.setProgress(modelCategory.getPercentCorrect(), modelCategory.getTotalQuestions());

        float wrongProgress = (float) (modelCategory.getAnsNoQue() * 100) / modelCategory.getTotalQuestions();

        holder.customProgress.setWrongAns(wrongProgress, modelCategory.getAnsNoQue() + modelCategory.getAnsYesQue());
        holder.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (catClickListener != null) {
                    catClickListener.onCatClick(catList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlClick;
        ImageView ivCatImage;
        TextView tvCatTitle, tvCatDetail, tvPassDes;
        CustomProgress customProgress;
        RelativeLayout rlBgColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            rlClick = itemView.findViewById(R.id.rltClick);
            ivCatImage = itemView.findViewById(R.id.iconCategory);
            tvCatTitle = itemView.findViewById(R.id.tvCatTitle);
            tvCatDetail = itemView.findViewById(R.id.tvDescription);
            tvPassDes = itemView.findViewById(R.id.tvPassDes);

            customProgress = itemView.findViewById(R.id.progressBar);

            rlBgColor = itemView.findViewById(R.id.bg_head);
        }
    }
}
