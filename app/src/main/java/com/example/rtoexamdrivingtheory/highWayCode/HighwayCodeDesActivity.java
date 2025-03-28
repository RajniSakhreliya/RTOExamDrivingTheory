package com.example.rtoexamdrivingtheory.highWayCode;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.rtoexamdrivingtheory.highWayCode.adapter.ViewpagerAdapter;
import com.example.rtoexamdrivingtheory.model.HighWayCategories;
import com.example.rtoexamdrivingtheory.model.HighWayItemModel;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.example.rtoexamdrivingtheory.MainApplication;

import java.util.ArrayList;

public class HighwayCodeDesActivity extends AppCompatActivity {

    private ImageView mIvBack;
    private TextView mTvRule;
    private RoundCornerProgressBar mRoundCornerProgressBar;
    private ViewPager mViewpager;
    private LinearLayout mLoadingView;
    private RelativeLayout mNavViewRight;
    private RecyclerView mList;
    private Activity activity;
    private HighWayCategories highWayCategories;
    private SqlLiteHighwayHelper sqlLiteHighwayHelper;
    private ArrayList<HighWayItemModel> listOfHighwayRules = new ArrayList<>();
    private String TAG = "HighwayCodeDes";
    private ViewpagerAdapter viewpagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_code_des);

        activity = HighwayCodeDesActivity.this;

        WindowUtils.setStatusBarLight(activity, R.color.grey10);

        initView();

        sqlLiteHighwayHelper = MainApplication.getSqlLiteHighwayHelper();

        highWayCategories = (HighWayCategories) getIntent().getSerializableExtra("modelCategory");

        mRoundCornerProgressBar.setProgress(highWayCategories.getPercentComplete());

        getAllRule();

        viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), activity);
        mViewpager.setAdapter(viewpagerAdapter);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setProgressOfBar(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getAllRule() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                listOfHighwayRules.clear();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                listOfHighwayRules = sqlLiteHighwayHelper.getHighwayCodesByCategory(highWayCategories);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setProgressOfBar(0);
                viewpagerAdapter.setList(listOfHighwayRules);

                if (highWayCategories.getCurrentIndex() > 0) {
                    mViewpager.setCurrentItem(highWayCategories.getCurrentIndex()==(listOfHighwayRules.size()-1)?0:highWayCategories.getCurrentIndex(), false);
                }
            }
        }.execute();
    }

    private void setProgressOfBar(int position) {
        sqlLiteHighwayHelper.setSeenHighwayCatItem(listOfHighwayRules.get(position));
        mRoundCornerProgressBar.setProgress((sqlLiteHighwayHelper.getCategoryPercent(highWayCategories) * 100) / highWayCategories.getTotalCards());
        mTvRule.setText(listOfHighwayRules.get(position).getShort_title());
        sqlLiteHighwayHelper.setCurrentIndexOfCategory(highWayCategories, position);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.ivBack);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvRule = (TextView) findViewById(R.id.tvRule);
        mRoundCornerProgressBar = (RoundCornerProgressBar) findViewById(R.id.roundCornerProgressBar);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mLoadingView = (LinearLayout) findViewById(R.id.loading_view);
        mNavViewRight = (RelativeLayout) findViewById(R.id.nav_view_right);
        mList = (RecyclerView) findViewById(R.id.list);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

}