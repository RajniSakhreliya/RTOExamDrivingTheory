package com.example.rtoexamdrivingtheory.hazardTest.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rtoexamdrivingtheory.R;
import com.rey.material.widget.FrameLayout;

public class DialogHazardTestResult extends Dialog {
    private final String content;
    private final String positiveStr;
    private final String negativeStr;
    Activity activity;
    TextView tvDescription;
    FrameLayout btnOk;
    private OnDialogDismissListener listener;
    private FrameLayout btnCancel;
    private TextView tvOk;
    private TextView tvCancel;

    public DialogHazardTestResult(@NonNull Activity context, String content, String positiveString, String negativeString, OnDialogDismissListener onDialogDismissListener) {
        super(context);
        this.activity = context;
        this.content = content;
        this.positiveStr = positiveString;
        this.negativeStr = negativeString;
        this.listener = onDialogDismissListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_hazard_test_result, null);

        requestWindowFeature(1);

        setContentView(dialogView);

        getWindow().setBackgroundDrawableResource(17170445);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        initView(dialogView);

        tvDescription.setText(content);

        tvOk.setText(positiveStr);
        tvCancel.setText(negativeStr);
    }

    private void initView(View dialogView) {
        tvDescription = dialogView.findViewById(R.id.tvDescription);

        tvOk = dialogView.findViewById(R.id.tv_ok);

        tvCancel = dialogView.findViewById(R.id.tvCancel);

        btnOk = dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onResultListener();
                dismiss();
            }
        });

        btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onDismiss();
                dismiss();
            }
        });
    }

    public interface OnDialogDismissListener {
        default void onDismiss(){}

        default void onResultListener(){}
    }
}
