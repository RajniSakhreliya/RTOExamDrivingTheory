package com.example.rtoexamdrivingtheory.testPractice.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rtoexamdrivingtheory.testPractice.AnswerClickListener;
import com.example.rtoexamdrivingtheory.model.QuestionModel;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.Logger;
import com.example.rtoexamdrivingtheory.utils.Utils;

import static com.example.rtoexamdrivingtheory.testPractice.MockTestPracticeActivity.isEndTest;

public class FragmentQuestion extends Fragment {

    private final QuestionModel questionModel;
    private final int currentFraPos;
    private TextView mTvQuestion;
    private ImageView mIvQuestionImage;
    private RecyclerView mRcvAnswers;
    public LinearLayout mLlExplanation;
    boolean isExplanationVisible = false;
    private Context context;
    public AdapterAnswer adapterAnswer;
    AnswerClickListener answerClickListener;
    SharedPreferences answerPreference;
    SharedPreferences.Editor answerEditor;
    private String TAG = "FragmentQue";
    private TextView mTvExplain;

    public FragmentQuestion(QuestionModel question, boolean isExplanationVisible, int currentFraPos) {
        this.questionModel = question;
        this.isExplanationVisible = isExplanationVisible;
        this.currentFraPos = currentFraPos;
    }

    public void setListener(AnswerClickListener answerClickListener) {
        this.answerClickListener = answerClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isEndTest) {
            isExplanationVisible = true;
        } else {
            isExplanationVisible = false;
        }

        answerPreference = context.getSharedPreferences(Utils.SHARED_PREFERENCE_ANSWERS, 0);

        mTvQuestion.setText(questionModel.getQuestionTitle());

        if (questionModel.getQuestionImage() != null) {
            String replace = questionModel.getQuestionImage().toLowerCase().replace(".png", "");
            Resources resources = getResources();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("drawable/");
            sb3.append(replace);
            int identifier = resources.getIdentifier(sb3.toString(), null, getContext().getPackageName());
            mIvQuestionImage.setVisibility(View.VISIBLE);
            if (identifier != 0) {
                Glide.with(context).load(identifier).into(mIvQuestionImage);
            }
        } else {
            mIvQuestionImage.setVisibility(View.GONE);
        }

        if (isExplanationVisible) {
            mLlExplanation.setVisibility(View.VISIBLE);
        } else {
            mLlExplanation.setVisibility(View.GONE);
        }

        adapterAnswer = new AdapterAnswer((Activity) context, questionModel.getAnswerList());
        adapterAnswer.setAnswerClickListener(new AnswerClickListener() {
            @Override
            public void onAnswerClicked(int ansOptionPosition, boolean isTrue, int queId) {
                answerClickListener.onAnswerClicked(ansOptionPosition, isTrue, queId);
            }
        });
        Logger.e(TAG, currentFraPos + "." + questionModel.getQuestionId());
        adapterAnswer.setSelectedOption(answerPreference.getInt(currentFraPos + "." + questionModel.getQuestionId(), -1));
        mRcvAnswers.setLayoutManager(new LinearLayoutManager(context));
        mRcvAnswers.setItemAnimator(new DefaultItemAnimator());
        mRcvAnswers.setAdapter(adapterAnswer);

        mTvExplain.setText(questionModel.getQuestionExplanation());

//        if (answerType == QuestionModel.answerModel.IMAGE) {
//            String replace = answerText.toLowerCase().replace(".png", "");
//            Resources resources = context.getResources();
//            StringBuilder sb = new StringBuilder();
//            sb.append("drawable/");
//            sb.append(replace);
//            int identifier = resources.getIdentifier(sb.toString(), null, context.getPackageName());
//            if (identifier != 0) {
//                Picasso.get().load(identifier).into(imageView);
//            }
//            imageView.setVisibility(View.VISIBLE);
//            textView.setText(String.valueOf(cursor.getPosition() + 1));
//            if (VERSION.SDK_INT >= 17) {
//                textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//                return;
//            }
//            return;
//        }


//        int answerCount = answerAdapter.getCount();
//        for (int i6 = 0; i6 < answerCount; i6++) {
//            View view = this.answerAdapter.getView(i6, null, null);
//            int finalI = queId;
//            view.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    TextView textView = (TextView) view.findViewById(R.id.quizTitle);
//                    String charSequence = textView.getText().toString();
//                    Boolean isCorrect = Boolean.valueOf(false);
//                    Object tag = textView.getTag();
//                    if ("Your insurance".equals(charSequence)) {
//                        isCorrect = Boolean.valueOf(true);
//                    }
//                    if ((tag instanceof String) && tag.equals("correct")) {
//                        isCorrect = Boolean.valueOf(true);
//                    }
//                    e();
//                    if (isCorrect) {
//                        Question.a(finalI, categorySelected, (MainApplication) getApplication(), Boolean.valueOf(true));
//                        numberCorrect++;
//                        textView.setTextColor(getResources().getColor(R.color.colorGreen));
//                        AlertDialog create = new AlertDialog.Builder(PlayActivity.this).create();
//                        create.setTitle("Correct!");
//                        create.setMessage(d());
//                        create.setButton(-1, "Next Question", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                                nextQuestion();
//                            }
//                        });
//                        create.setButton(-3, "Review Answers", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        });
//                        create.show();
//                        return;
//                    }
//                    Question.a(finalI, categorySelected, (MainApplication) getApplication(), Boolean.valueOf(false));
//                    numberIncorrect++;
//                    textView.setTextColor(getResources().getColor(R.color.redColor));
//                    AlertDialog create2 = new AlertDialog.Builder(PlayActivity.this).create();
//                    create2.setTitle("Whoops!");
//                    create2.setMessage("That's not right.");
//                    create2.setButton(-1, "Next Question", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            nextQuestion();
//                        }
//                    });
//                    create2.setButton(-3, "Review Answers", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            e();
//                        }
//                    });
//                    create2.show();
//                }
//            });
    }

    private void initView(View view) {
        mTvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        mIvQuestionImage = (ImageView) view.findViewById(R.id.ivQuestionImage);
        mRcvAnswers = (RecyclerView) view.findViewById(R.id.rcvAnswers);
        mLlExplanation = (LinearLayout) view.findViewById(R.id.llExplanation);
        mTvExplain = (TextView) view.findViewById(R.id.tvExplain);
    }

}
