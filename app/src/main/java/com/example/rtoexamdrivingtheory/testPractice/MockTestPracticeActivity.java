package com.example.rtoexamdrivingtheory.testPractice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.rtoexamdrivingtheory.testPractice.dialog.EndTestDialog;
import com.example.rtoexamdrivingtheory.testPractice.dialog.TestExitDialog;
import com.example.rtoexamdrivingtheory.model.AnswerModel;
import com.example.rtoexamdrivingtheory.model.QuestionModel;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.PreferencesUtils;
import com.example.rtoexamdrivingtheory.utils.Utils;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.revisionquizmaker.drivingtheorytestrevision.Question;
import com.example.rtoexamdrivingtheory.MainApplication;

import java.util.ArrayList;
import java.util.Random;

public class MockTestPracticeActivity extends AppCompatActivity implements View.OnClickListener, AnswerClickListener {

    private ImageView mIvBack;
    private TextView mTvEndTest;
    private ImageView mIvPreviousQue;
    private TextView mTvQuestionNumber;
    private ImageView mIvNextQuestion;
    private ViewPager mQuestionViewpager;
    private QuestionPagerAdapter mQuestionViewpagerAdapter;
    private int categorySelected;
    private int questionSelected;
    private boolean paidVersion = false;
    private String mockTest;
    private int numberCorrect = 0;
    private int numberIncorrect = 0;
    ArrayList<QuestionModel> listOfQue = new ArrayList<>();
    private Activity activity;
    private String TAG = "MockTest";
    public static boolean isEndTest = false;
    SharedPreferences answerPreference;
    SharedPreferences.Editor answerEditor;
    private String categoryTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = MockTestPracticeActivity.this;

        isEndTest = false;

        WindowUtils.setStatusBarLight(activity, R.color.grey10);

        setContentView(R.layout.activity_mock_test_practice);

        initView();

        mQuestionViewpagerAdapter = new QuestionPagerAdapter(activity,getSupportFragmentManager());

        getQuestions();

        mIvPreviousQue.setColorFilter(getResources().getColor(R.color.grey80));
        mIvNextQuestion.setColorFilter(getResources().getColor(R.color.colorGreen));

        mQuestionViewpager.setAdapter(mQuestionViewpagerAdapter);
        mQuestionViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIvPreviousQue.setColorFilter(getResources().getColor(R.color.grey80));
                    mIvNextQuestion.setColorFilter(getResources().getColor(R.color.colorGreen));
                } else if (position == 49) {
                    mIvPreviousQue.setColorFilter(getResources().getColor(R.color.colorGreen));
                    mIvNextQuestion.setColorFilter(getResources().getColor(R.color.grey80));
                } else {
                    mIvPreviousQue.setColorFilter(getResources().getColor(R.color.colorGreen));
                    mIvNextQuestion.setColorFilter(getResources().getColor(R.color.colorGreen));
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTvQuestionNumber.setText(mQuestionViewpagerAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mQuestionViewpager.setCurrentItem(0);

        mQuestionViewpagerAdapter.setListener(this);

        answerPreference = getApplicationContext().getSharedPreferences(Utils.SHARED_PREFERENCE_ANSWERS, 0); // 0 - for private mode
        answerEditor = answerPreference.edit();
        answerEditor.clear();
        answerEditor.commit();
    }

    private synchronized void getQuestions() {
        Intent intent = getIntent();

        categorySelected = intent.getIntExtra("categorySelected", -2);
        questionSelected = intent.getIntExtra("questionSelected", -2);
        paidVersion = PreferencesUtils.getInstance(activity).checkPurchase();
        mockTest = intent.getStringExtra("mockTest");
        categoryTest = intent.getStringExtra("categoryTest");

        if (intent.getSerializableExtra("questionsShown") != null) {
//            questionShown = (ArrayList) intent.getSerializableExtra("questionsShown");
        }

        if (mockTest != null && mockTest.equals("YES")) {
            ArrayList<Double> quesCountList = new ArrayList<>();
            while (true) {
                QuestionModel questionModel = new QuestionModel();

                categorySelected = new Random().nextInt((paidVersion ? 14 : 3) - 1) + 1;

                Cursor questionCorrespondingQuiz = Question.questionCorrespondingQuiz(categorySelected, (MainApplication) getApplication());
                int count = questionCorrespondingQuiz.getCount();
                questionCorrespondingQuiz.close();

                questionSelected = new Random().nextInt(count - 1) + 1;

                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(categorySelected));
                sb.append(".");
                sb.append(Integer.toString(questionSelected));
                double parseDouble = Double.parseDouble(sb.toString());
                if (!quesCountList.contains(parseDouble)) {
                    if (quesCountList.size() < 50) {
                        quesCountList.add(Double.valueOf(parseDouble));
                    } else {
                        break;
                    }

                    Cursor questionCursor = Question.questionCorrespondingQuiz(categorySelected, (MainApplication) getApplication());
                    if (questionCursor.moveToPosition(questionSelected)) {
                        String questionTitle = questionCursor.getString(questionCursor.getColumnIndexOrThrow("question_title"));
                        String questionExplanation = questionCursor.getString(questionCursor.getColumnIndexOrThrow("question_explanation"));
                        int queId = questionCursor.getInt(questionCursor.getColumnIndexOrThrow("_id"));
                        String queImagePath = questionCursor.getString(questionCursor.getColumnIndexOrThrow("question_image_path"));

                        questionModel.setQuestionTitle(questionTitle);
                        questionModel.setQuestionExplanation(questionExplanation);
                        questionModel.setQuestionId(queId);
                        questionModel.setQuestionImage(queImagePath);

//                    ------------------Answer--------------------
                        Cursor answerCursor = Question.answerCorrespondingQuestion(queId, (MainApplication) getApplication());
                        ArrayList<AnswerModel> listAnswer = new ArrayList<>();
                        while (answerCursor.moveToNext()) {
                            AnswerModel answerModel = new AnswerModel();
                            String answerText = answerCursor.getString(answerCursor.getColumnIndexOrThrow("answer_text"));
                            int answerType = answerCursor.getInt(answerCursor.getColumnIndexOrThrow("ANSWER_TYPE"));
                            boolean isCorrect;
                            if (answerCursor.getInt(answerCursor.getColumnIndexOrThrow("ANSWER_IS_CORRECT")) == 1) {
                                isCorrect = true;
                            } else {
                                isCorrect = false;
                            }

                            answerModel.setAnswerText(answerText);
                            answerModel.setAnswerType(answerType);
                            answerModel.setCorrect(isCorrect);
                            answerModel.setQueId(queId);
                            listAnswer.add(answerModel);
                        }

                        questionModel.setAnswerList(listAnswer);

                        listOfQue.add(questionModel);
                    }
                }
            }
            mQuestionViewpagerAdapter.setQueList(listOfQue);
        } else if (categoryTest != null) {
            ArrayList<Double> quesCountList = new ArrayList<>();

            categorySelected = Integer.parseInt(categoryTest);
            Cursor questionCorrespondingQuiz = Question.questionCorrespondingQuiz(categorySelected, (MainApplication) getApplication());
            int count = questionCorrespondingQuiz.getCount();
            questionCorrespondingQuiz.close();

            for (int i = 0; i < count; i++) {
                questionSelected = i;
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toString(categorySelected));
                sb.append(".");
                sb.append(Integer.toString(questionSelected));
                double parseDouble = Double.parseDouble(sb.toString());
                quesCountList.add(Double.valueOf(parseDouble));

                Cursor questionCursor = Question.questionCorrespondingQuiz(categorySelected, (MainApplication) getApplication());
                if (questionCursor.moveToPosition(questionSelected)) {
                    QuestionModel questionModel = new QuestionModel();

                    String questionTitle = questionCursor.getString(questionCursor.getColumnIndexOrThrow("question_title"));
                    String questionExplanation = questionCursor.getString(questionCursor.getColumnIndexOrThrow("question_explanation"));
                    int queId = questionCursor.getInt(questionCursor.getColumnIndexOrThrow("_id"));
                    String queImagePath = questionCursor.getString(questionCursor.getColumnIndexOrThrow("question_image_path"));

                    questionModel.setQuestionTitle(questionTitle);
                    questionModel.setQuestionExplanation(questionExplanation);
                    questionModel.setQuestionId(queId);
                    questionModel.setQuestionImage(queImagePath);

//                    ------------------Answer--------------------
                    Cursor answerCursor = Question.answerCorrespondingQuestion(queId, (MainApplication) getApplication());
                    ArrayList<AnswerModel> listAnswer = new ArrayList<>();
                    while (answerCursor.moveToNext()) {
                        AnswerModel answerModel = new AnswerModel();
                        String answerText = answerCursor.getString(answerCursor.getColumnIndexOrThrow("answer_text"));
                        int answerType = answerCursor.getInt(answerCursor.getColumnIndexOrThrow("ANSWER_TYPE"));
                        boolean isCorrect;
                        if (answerCursor.getInt(answerCursor.getColumnIndexOrThrow("ANSWER_IS_CORRECT")) == 1) {
                            isCorrect = true;
                        } else {
                            isCorrect = false;
                        }

                        answerModel.setAnswerText(answerText);
                        answerModel.setAnswerType(answerType);
                        answerModel.setCorrect(isCorrect);
                        answerModel.setQueId(queId);
                        listAnswer.add(answerModel);
                    }

                    questionModel.setAnswerList(listAnswer);

                    listOfQue.add(questionModel);
                }
            }
            mQuestionViewpagerAdapter.setQueList(listOfQue);
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.noQueFound), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }

    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.ivBack);
        mIvBack.setOnClickListener(this);

        mTvEndTest = findViewById(R.id.tvEndTest);
        mTvEndTest.setOnClickListener(this);

        mIvPreviousQue = (ImageView) findViewById(R.id.ivPreviousQue);
        mIvPreviousQue.setOnClickListener(this);

        mTvQuestionNumber = (TextView) findViewById(R.id.tvQuestionNumber);
        mTvQuestionNumber.setOnClickListener(this);

        mIvNextQuestion = (ImageView) findViewById(R.id.ivNextQuestion);
        mIvNextQuestion.setOnClickListener(this);

        mQuestionViewpager = (ViewPager) findViewById(R.id.questionViewpager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.tvEndTest) {
            endTest();
        } else if (id == R.id.ivPreviousQue) {
            prevQue();
        } else if (id == R.id.ivNextQuestion) {
            nextQues();
        } else if (id == R.id.tvQuestionNumber) {
        }
    }

    private void nextQues() {
        mQuestionViewpager.setCurrentItem(mQuestionViewpager.getCurrentItem() + 1, true);
    }

    private void prevQue() {
        mQuestionViewpager.setCurrentItem(mQuestionViewpager.getCurrentItem() - 1, true);
    }

    private void openInfoDialog() {
    }

    private void endTest() {
        EndTestDialog endTest = new EndTestDialog(activity);

        endTest.setOnPositiveClick(new TestExitDialog.onPositiveBtnClick() {
            @Override
            public void onPositiveBtnClick() {
                isEndTest = true;
                mQuestionViewpagerAdapter.endTest();
                endTest.dismiss();
            }
        });
        endTest.setOnNegativeClick(new TestExitDialog.onNegativeBtnClick() {
            @Override
            public void onNegativeBtnClick() {
                endTest.dismiss();
            }
        });

        endTest.show();
    }

    @Override
    public void onAnswerClicked(int ansOptionPosition, boolean isTrue, int queId) {
        answerEditor.putInt(mQuestionViewpager.getCurrentItem() + "." + queId, ansOptionPosition);
        answerEditor.commit();
        Question.setAns(queId, categorySelected, (MainApplication) getApplication(), isTrue);
        numberCorrect++;

    }

    @Override
    public void onBackPressed() {
        if (!isEndTest) {
            endTest();
            return;
        }

        setResult(RESULT_OK);
        finish();
    }
}