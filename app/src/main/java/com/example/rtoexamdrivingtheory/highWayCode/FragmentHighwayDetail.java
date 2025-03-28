package com.example.rtoexamdrivingtheory.highWayCode;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rtoexamdrivingtheory.model.HighWayItemModel;
import com.example.rtoexamdrivingtheory.R;

public class FragmentHighwayDetail extends Fragment {

    private final HighWayItemModel highWayItemModel;
    WebView webView;

    public FragmentHighwayDetail(HighWayItemModel highWayItemModel) {
        this.highWayItemModel = highWayItemModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_higway_code_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        webView = view.findViewById(R.id.webView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        highWayItemModel.setFull_html(highWayItemModel.getFull_html().replaceAll("/android_asset/", "/android_asset/theory/highway/detail/"));
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("file:///android_asset/highway/detail/", highWayItemModel.getFull_html(), "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                try {
                    if (webResourceRequest.getUrl().toString().startsWith("fb295307084145561")) {
                        String[] split = webResourceRequest.getUrl().toString().split("/");
//                        W(highWayItemModel,split[split.length - 1]);
                        return true;
                    }

                    String uri = webResourceRequest.getUrl().toString();
                    if (webResourceRequest.getUrl().toString().equals("file:///guidance/the-highway-code/road-markings")) {
                        uri = "https://assets.digital.cabinet-office.gov.uk/media/560aa6c7ed915d035900001a/the-highway-code-road-markings.pdf";
                    }
                    getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(uri)));
                    return true;
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }


}
