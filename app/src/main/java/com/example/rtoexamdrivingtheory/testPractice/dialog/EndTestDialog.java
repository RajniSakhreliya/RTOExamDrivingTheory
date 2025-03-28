package com.example.rtoexamdrivingtheory.testPractice.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.rtoexamdrivingtheory.R;

public class EndTestDialog extends Dialog {

    final Activity activity;
    TextView mTvTitle;
    TextView mTvMessage;
    TextView mBtnNegative;
    TextView mBtnPositive;
    private CardView mCvMain;

    public EndTestDialog(@NonNull Activity context) {
        super(context);
        activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View dialogView = activity.getLayoutInflater().inflate(R.layout.test_exit_dialog, null);

        setContentView(dialogView);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        mCvMain = dialogView.findViewById(R.id.cvMain);
        mTvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        mTvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
        mBtnNegative = (TextView) dialogView.findViewById(R.id.btnNegative);
        mBtnPositive = (TextView) dialogView.findViewById(R.id.btnPositive);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 0);
        mCvMain.setLayoutParams(params);
        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPositiveClick != null) onPositiveClick.onPositiveBtnClick();
            }
        });

        mBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onNegativeClick != null) onNegativeClick.onNegativeBtnClick();
            }
        });

        setTitle(activity.getResources().getString(R.string.endTest));
        setMessage(activity.getResources().getString(R.string.doYouWantToEndTest));
        setPositiveBtn(activity.getResources().getString(R.string.text_ok));
        setNegativeBtn(activity.getResources().getString(R.string.text_cancel));
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setMessage(String message) {
        mTvMessage.setText(message);
    }

    public void setPositiveBtn(String str) {
        mBtnPositive.setText(str);
    }

    public void setNegativeBtn(String str) {
        mBtnNegative.setText(str);
    }

    TestExitDialog.onPositiveBtnClick onPositiveClick;

    public void setOnPositiveClick(TestExitDialog.onPositiveBtnClick onPositiveClick) {
        this.onPositiveClick = onPositiveClick;
    }

    TestExitDialog.onNegativeBtnClick onNegativeClick;

    public void setOnNegativeClick(TestExitDialog.onNegativeBtnClick onNegativeClick) {
        this.onNegativeClick = onNegativeClick;
    }

    private void initView() {
        mCvMain = (CardView) findViewById(R.id.cvMain);
    }

    public interface onPositiveBtnClick {
        void onPositiveBtnClick();
    }

    public interface onNegativeBtnClick {
        void onNegativeBtnClick();
    }
}
