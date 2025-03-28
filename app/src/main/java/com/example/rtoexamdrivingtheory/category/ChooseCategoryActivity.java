package com.example.rtoexamdrivingtheory.category;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rtoexamdrivingtheory.category.adapter.AdapterCategory;
import com.example.rtoexamdrivingtheory.model.ModelCategory;
import com.example.rtoexamdrivingtheory.testPractice.MockTestPracticeActivity;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.recyclerViewBounce.RecyclerViewBouncy;
import com.example.rtoexamdrivingtheory.utils.PreferencesUtils;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.revisionquizmaker.drivingtheorytestrevision.Question;
import com.revisionquizmaker.drivingtheorytestrevision.SqlLiteAssetHelper;
import com.example.rtoexamdrivingtheory.MainApplication;

import java.util.ArrayList;

import static com.example.rtoexamdrivingtheory.Constant.SELECT_QUESTION_ACTIVITY;

public class ChooseCategoryActivity extends AppCompatActivity implements AdapterCategory.CatClickListener, View.OnClickListener {

    private RecyclerViewBouncy mRcvSelectCategory;
    private Activity activity;
    SqlLiteAssetHelper sqlLiteAssetHelper;
    public Cursor cursor;
    AdapterCategory adapterCategory;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ChooseCategoryActivity.this;

        WindowUtils.setStatusBarLight(activity, R.color.grey10);

        setContentView(R.layout.activity_choose_category);

        initView();

        MainApplication mainApplication = (MainApplication) getApplication();
        sqlLiteAssetHelper = new SqlLiteAssetHelper(getApplicationContext());
        Question.copyDatabase(sqlLiteAssetHelper, mainApplication.sqlLiteAssetFeedHelper);
        PreferencesUtils.getInstance(activity).setBoolean("hasCopiedScore", true);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(getResources().getString(R.string.gettingData));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        adapterCategory = new AdapterCategory(activity);
        adapterCategory.setListener(this);
        mRcvSelectCategory.setAdapter(adapterCategory);

        new getCatAsync().execute(new String[]{""});
    }

    private void initView() {
        ImageView mIvBack = (ImageView) findViewById(R.id.ivBack);
        mIvBack.setOnClickListener(this);

        ImageView mIvRefresh = (ImageView) findViewById(R.id.ivRefresh);
        mIvRefresh.setOnClickListener(this);

        mRcvSelectCategory = (RecyclerViewBouncy) findViewById(R.id.rcvSelectCategory);
    }

    public void onResume() {
        super.onResume();
    }

    ArrayList<ModelCategory> listOfCat = new ArrayList<>();

    @Override
    public void onCatClick(ModelCategory modelCategory) {
        Intent intent = new Intent(getApplication().getBaseContext(), MockTestPracticeActivity.class);
        intent.putExtra("mockTest", "NO");
        intent.putExtra("fullVersion", true);
        intent.putExtra("numberCorrect", 0);
        intent.putExtra("numberIncorrect", 0);
        intent.putExtra("categoryTest", String.valueOf(modelCategory.getQuizNumber()));
        startActivityForResult(intent, SELECT_QUESTION_ACTIVITY);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId()   ;
        if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.ivRefresh) {
            new getCatAsync().execute(new String[]{""});
        }
    }

    public class getCatAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.show();
            }
        }

        public String doInBackground(String... strArr) {
            Question.copyDatabase((MainApplication) getApplication(), (AsyncTask) this);
            return "Executed";
        }

        public void onPostExecute(String str) {
            cursor = Question.createCursor((MainApplication) getApplication());

            listOfCat.clear();

            while (cursor.moveToNext()) {
                ModelCategory modelCategory = new ModelCategory();
                modelCategory.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                modelCategory.setQuizNumber(Integer.parseInt(Integer.toString(cursor.getPosition() + 1)));

                String percent = cursor.getString(cursor.getColumnIndexOrThrow("category_percentage_correct"));

                Log.e("CATEGORYNAME", "onPostExecute: " + modelCategory.getCategoryName());
                if (percent == null) {
                    modelCategory.setPercentCorrect(0);
                } else {
                    modelCategory.setPercentCorrect(Float.parseFloat(percent));
                }
                modelCategory.setCatImage(null);
                modelCategory.setTotalQuestions(Question.questionCorrespondingCategorySize(cursor.getPosition() + 1, (MainApplication) getApplication()));

                Cursor quizQueCursor = Question.questionCorrespondingQuiz(modelCategory.getQuizNumber(), (MainApplication) activity.getApplication());

                //WrongAns Percentage Count
                int notAttempted = 0;
                int yesAns = 0;
                int noAns = 0;
                while (quizQueCursor.moveToNext()) {
                    String isAnsCorrectly = quizQueCursor.getString(quizQueCursor.getColumnIndexOrThrow("questionAnsweredCorrectly"));
                    if (isAnsCorrectly == null) {
                        notAttempted++;
                    } else if (isAnsCorrectly.matches("NO")) {
                        noAns++;
                    } else if (isAnsCorrectly.matches("YES")) {
                        yesAns++;
                    }
                }

                modelCategory.setAnsNotAttempted(notAttempted);
                modelCategory.setAnsYesQue(yesAns);
                modelCategory.setAnsNoQue(noAns);

                Log.e("CategoryModel", "onPostExecute: " + notAttempted + "Y : " + yesAns + " N : " + noAns);

                listOfCat.add(modelCategory);
            }

            setAdapter();
        }
    }

    private void setAdapter() {
        adapterCategory.setList(listOfCat);
        progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_QUESTION_ACTIVITY && resultCode == RESULT_OK) {
            new getCatAsync().execute(new String[]{""});
        }
    }
}