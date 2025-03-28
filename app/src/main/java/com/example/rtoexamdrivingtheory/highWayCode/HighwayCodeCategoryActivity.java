package com.example.rtoexamdrivingtheory.highWayCode;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rtoexamdrivingtheory.Constant;
import com.example.rtoexamdrivingtheory.highWayCode.adapter.AdapterHighwayCodeCategory;
import com.example.rtoexamdrivingtheory.model.HighWayCategories;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.recyclerViewBounce.RecyclerViewBouncy;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.example.rtoexamdrivingtheory.MainApplication;

import java.util.ArrayList;

public class HighwayCodeCategoryActivity extends AppCompatActivity {

    private SqlLiteHighwayHelper sqlLiteHighwayHelper;
    private ImageView mIvBack;
    private RecyclerViewBouncy mRcvSelectCategory;
    AdapterHighwayCodeCategory adapter;
    private Activity activity;
    private ArrayList<HighWayCategories> listOfCat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_code);

        activity = HighwayCodeCategoryActivity.this;

        WindowUtils.setStatusBarLight(activity, R.color.grey10);

        sqlLiteHighwayHelper = MainApplication.getSqlLiteHighwayHelper();

        initView();

        getCategoryList();
    }

    private void getCategoryList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                listOfCat.clear();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                listOfCat = sqlLiteHighwayHelper.getHighWayCategories();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.setCategoryList(listOfCat);
            }
        }.execute();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.ivBack);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRcvSelectCategory = (RecyclerViewBouncy) findViewById(R.id.rcvSelectCategory);
        mRcvSelectCategory.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new AdapterHighwayCodeCategory(activity);
        adapter.setListener(new AdapterHighwayCodeCategory.onItemClickListener() {
            @Override
            public void onItemClick(HighWayCategories highWayCategories) {
                startActivityForResult(new Intent(activity, HighwayCodeDesActivity.class)
                        .putExtra("modelCategory", highWayCategories), Constant.HIGHWAY_CODE_DES_ACTIVITY);
            }
        });

        mRcvSelectCategory.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.HIGHWAY_CODE_DES_ACTIVITY && resultCode == RESULT_OK) {
            getCategoryList();
        }
    }
}