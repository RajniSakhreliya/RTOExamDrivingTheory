package com.example.rtoexamdrivingtheory.trafficAndRoadSign;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rtoexamdrivingtheory.model.ModelTraffic;
import com.example.rtoexamdrivingtheory.R;
import com.example.rtoexamdrivingtheory.utils.AssetsUtil;
import com.rey.material.widget.FrameLayout;

public class DialogSign extends Dialog {
    private ImageView ivSign;
    private TextView tvSignDes;
    private FrameLayout frOk;
    private ModelTraffic data;
    private Activity activity;

    public DialogSign(@NonNull Activity context, ModelTraffic modelTraffic) {
        super(context);
        this.activity = context;
        this.data = modelTraffic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sign, null);

        requestWindowFeature(1);
        setContentView(dialogView);

        getWindow().setBackgroundDrawableResource(17170445);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        initView(dialogView);

        ivSign.setImageBitmap(AssetsUtil.getBitmapFromAssetFile(activity, "theory/sign/" + data.getSign() + ".webp"));
        tvSignDes.setText(data.getName());
    }

    private void initView(View dialogView) {
        ivSign = dialogView.findViewById(R.id.imgSign);
        tvSignDes = dialogView.findViewById(R.id.tvContent);
        frOk = dialogView.findViewById(R.id.btn_ok);
        frOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
