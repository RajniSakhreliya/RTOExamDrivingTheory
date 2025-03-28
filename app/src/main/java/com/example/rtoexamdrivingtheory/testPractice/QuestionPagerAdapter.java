package com.example.rtoexamdrivingtheory.testPractice;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rtoexamdrivingtheory.testPractice.fragment.FragmentQuestion;
import com.example.rtoexamdrivingtheory.model.QuestionModel;
import com.example.rtoexamdrivingtheory.R;

import java.util.ArrayList;

public class QuestionPagerAdapter extends FragmentPagerAdapter {
    ArrayList<QuestionModel> queList = new ArrayList<>();
    String TAG = "QuePager";
    AnswerClickListener answerClickListener;
    FragmentManager fragmentManager;
    ArrayList<FragmentQuestion> listOfFragment = new ArrayList<>();
    Activity activity;

    public QuestionPagerAdapter(Activity activity,FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.activity=activity;
    }

    public void setListener(AnswerClickListener answerClickListener) {
        this.answerClickListener = answerClickListener;
    }

    public void setQueList(ArrayList<QuestionModel> queList) {
        this.queList = queList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return queList.size();
    }

    @Override
    public Fragment getItem(int position) {
        FragmentQuestion fragment = new FragmentQuestion(queList.get(position), false, position);
        fragment.setListener(answerClickListener);
        listOfFragment.add(fragment);
        return listOfFragment.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  activity.getResources().getString(R.string.question)+" "+ (position + 1) + " "+activity.getResources().getString(R.string.of) +" "+ getCount();
    }

    public void endTest() {
        for (int i = 0; i < listOfFragment.size(); i++) {
            ((FragmentQuestion) listOfFragment.get(i)).adapterAnswer.notifyDataSetChanged();
            ((FragmentQuestion) listOfFragment.get(i)).mLlExplanation.setVisibility(View.VISIBLE);
        }
    }
}