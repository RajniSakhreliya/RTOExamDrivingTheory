package com.example.rtoexamdrivingtheory.testPractice.fragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rtoexamdrivingtheory.testPractice.AnswerClickListener;
import com.example.rtoexamdrivingtheory.model.AnswerModel;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.Logger;
import com.example.rtoexamdrivingtheory.utils.Utils;

import java.util.ArrayList;

import static com.example.rtoexamdrivingtheory.testPractice.MockTestPracticeActivity.isEndTest;

public class AdapterAnswer extends RecyclerView.Adapter<AdapterAnswer.ViewHolder> {

    private final Activity activity;
    ArrayList<AnswerModel> listOfAnswer;
    AnswerClickListener answerClickListener;
    int selectedOption = -1;
    Animation shake;
    private String TAG = "Answer : ";

    public AdapterAnswer(Activity activity, ArrayList<AnswerModel> listOfAnswer) {
        this.activity = activity;
        this.listOfAnswer = listOfAnswer;
        shake = AnimationUtils.loadAnimation(activity, R.anim.shake_animation);
    }

    public void setAnswerClickListener(AnswerClickListener answerClickListener) {
        this.answerClickListener = answerClickListener;
    }

    public void setSelectedOption(int i) {
        Logger.e(TAG, "" + i);
        this.selectedOption = i;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_option_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnswerModel answerModel = listOfAnswer.get(position);

        if (answerModel.getAnswerType() == answerModel.IMAGE) {
            holder.ivAnswer.setVisibility(View.VISIBLE);
            holder.tvAnswerText.setVisibility(View.GONE);

            String replace = answerModel.getAnswerText().toLowerCase().replace(".png", "");

            StringBuilder sb = new StringBuilder();
            sb.append("drawable/");
            sb.append(replace);

            int identifier = activity.getResources().getIdentifier(sb.toString(), null, activity.getPackageName());

            if (identifier != 0) {
                Glide.with(activity).load(identifier).into(holder.ivAnswer);
            }
        } else {
            holder.ivAnswer.setVisibility(View.GONE);
            holder.tvAnswerText.setVisibility(View.VISIBLE);
            holder.tvAnswerText.setText(answerModel.getAnswerText());
        }

        if (selectedOption == position) {
            holder.tvOptionNum.setSelected(true);
            holder.tvOptionNum.setTextColor(activity.getResources().getColor(R.color.white));

            if (!isEndTest) holder.ivAnswer.startAnimation(shake);
            if (!isEndTest) holder.tvAnswerText.startAnimation(shake);
        } else {
            holder.tvOptionNum.setSelected(false);
            holder.tvOptionNum.setTextColor(activity.getResources().getColor(R.color.black));
        }

        if (isEndTest) {
            if (answerModel.isCorrect())
                holder.tvAnswerText.setTextColor(activity.getResources().getColor(R.color.colorGreen));

            if (selectedOption == position) {
                if (answerModel.isCorrect()) {
                    holder.tvAnswerText.setTextColor(activity.getResources().getColor(R.color.colorGreen));
                } else {
                    holder.tvAnswerText.setTextColor(activity.getResources().getColor(R.color.redColor));
                }
            }
        }

        holder.tvOptionNum.setText(Utils.getAlphabet(position + 1));

    }

    @Override
    public int getItemCount() {
        return listOfAnswer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvOptionNum, tvAnswerText;
        ImageView ivAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnswer = itemView.findViewById(R.id.ivAnswer);
            tvOptionNum = itemView.findViewById(R.id.tvOptionNum);
            tvAnswerText = itemView.findViewById(R.id.tvOptionText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isEndTest) {
                        return;
                    }
                    selectedOption = getAdapterPosition();
                    answerClickListener.onAnswerClicked(getAdapterPosition(), listOfAnswer.get(getAdapterPosition()).isCorrect(), listOfAnswer.get(getAdapterPosition()).getQueId());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
