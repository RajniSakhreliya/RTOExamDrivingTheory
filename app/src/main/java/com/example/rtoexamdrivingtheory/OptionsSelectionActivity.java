package com.example.rtoexamdrivingtheory;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rtoexamdrivingtheory.category.ChooseCategoryActivity;
import com.example.rtoexamdrivingtheory.testPractice.MockTestPracticeActivity;
import com.example.rtoexamdrivingtheory.hazardTest.HazardCategoryActivity;
import com.example.rtoexamdrivingtheory.highWayCode.HighwayCodeCategoryActivity;
import com.example.rtoexamdrivingtheory.highWayCode.HighwayCodeDesActivity;
import com.example.rtoexamdrivingtheory.model.HighWayCategories;
import com.example.rtoexamdrivingtheory.model.HighWayItemModel;
import com.example.rtoexamdrivingtheory.trafficAndRoadSign.TrafficAndRoadSignsActivity;
import com.example.rtoexamdrivingtheory.utils.WindowUtils;
import com.rey.material.widget.RelativeLayout;

public class OptionsSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout cvFreeTheoryPracticeTest, cvQuestionsCat, cvHazardPerception, cvHighwayCode, cvTrafficRoadSignals;
    private Activity activity;
    WebView webView;
    RelativeLayout rlViewDetail;
    private HighWayCategories highwayCategories;
    private HighWayItemModel highWayItemModel;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_selection);
        activity = OptionsSelectionActivity.this;

        initView();

        WindowUtils.setStatusBarLight(activity, R.color.mainOptionSelection);

//        highwayCategories = MainApplication.getSqlLiteHighwayHelper().getHighwayCatRandom();
//        highWayItemModel = MainApplication.getSqlLiteHighwayHelper().getRandomHighWayRule(highwayCategories);

        setRandomHighWayData();
    }

    private void setRandomHighWayData() {
        if (highWayItemModel == null) {
            return;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("file:///android_asset/highway/detail/", highWayItemModel.getFull_html(), "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest webResourceRequest) {
                if (webResourceRequest.getUrl().toString().startsWith("fb295307084145561")) {
                    String[] split = webResourceRequest.getUrl().toString().split("/");
//                    MainActivity.this.V(split[split.length - 1]);
                    return true;
                }
                String uri = webResourceRequest.getUrl().toString();
                if (webResourceRequest.getUrl().toString().equals("file:///guidance/the-highway-code/road-markings")) {
                    uri = "https://assets.digital.cabinet-office.gov.uk/media/560aa6c7ed915d035900001a/the-highway-code-road-markings.pdf";
                }
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(uri)));
                return true;
            }
        });
    }

    private void initView() {
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        cvFreeTheoryPracticeTest = findViewById(R.id.cvFreeTheoryTestPractice);
        cvFreeTheoryPracticeTest.setOnClickListener(this);

        cvQuestionsCat = findViewById(R.id.cvQuestionByCategory);
        cvQuestionsCat.setOnClickListener(this);

        cvHazardPerception = findViewById(R.id.cvHazardPerception);
        cvHazardPerception.setOnClickListener(this);

        cvHighwayCode = findViewById(R.id.cvHighwayCodes);
        cvHighwayCode.setOnClickListener(this);

        cvTrafficRoadSignals = findViewById(R.id.cvTrafficSignals);
        cvTrafficRoadSignals.setOnClickListener(this);

        webView = findViewById(R.id.fragment_webview);

        rlViewDetail = findViewById(R.id.rltDetailHighway);
        rlViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HighwayCodeDesActivity.class);
                intent.putExtra("modelCategory", highwayCategories);
                intent.putExtra("highWayItemModel", highWayItemModel);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.cvFreeTheoryTestPractice) {
            Intent intent = new Intent(getApplication().getBaseContext(), MockTestPracticeActivity.class);
            intent.putExtra("mockTest", "YES");
            intent.putExtra("fullVersion", true);
            intent.putExtra("numberCorrect", 0);
            intent.putExtra("numberIncorrect", 0);
            startActivity(intent);
        } else if (id == R.id.cvQuestionByCategory) {
            startActivity(new Intent(this, ChooseCategoryActivity.class));
        } else if (id == R.id.cvHazardPerception) {
            startActivity(new Intent(activity, HazardCategoryActivity.class));
        } else if (id == R.id.cvHighwayCodes) {
            startActivity(new Intent(activity, HighwayCodeCategoryActivity.class));
        } else if (id == R.id.cvTrafficSignals) {
            startActivity(new Intent(activity, TrafficAndRoadSignsActivity.class));
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
}