package com.example.rtoexamdrivingtheory.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.example.rtoexamdrivingtheory.R;

public class CustomProgress extends View {

    private float wrongAns;
    private float progress;

    private float goalIndicatorHeight;
    private int goalReachedColor;
    private int goalNotReachedColor;
    private int unfilledSectionColor;
    private int barThickness;
    private ValueAnimator barAnimator;
    private int totalReached = 0;
    private int totaQue = 0;

    public CustomProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.GoalProgressBar, 0, 0);
        try {
            setGoalIndicatorHeight(typedArray.getDimensionPixelSize(R.styleable.GoalProgressBar_goalIndicatorHeight, 4));
            setGoalReachedColor(typedArray.getColor(R.styleable.GoalProgressBar_goalReachedColor, Color.GREEN));
            setGoalNotReachedColor(typedArray.getColor(R.styleable.GoalProgressBar_goalNotReachedColor, Color.RED));
            setUnfilledSectionColor(typedArray.getColor(R.styleable.GoalProgressBar_unfilledSectionColor, Color.GRAY));
            setBarThickness(typedArray.getDimensionPixelOffset(R.styleable.GoalProgressBar_barThickness, 4));
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        // save our added state - progress and goal
        bundle.putFloat("progress", progress);
        bundle.putFloat("goal", wrongAns);

        // save super state
        bundle.putParcelable("superState", super.onSaveInstanceState());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            // restore our added state - progress and goal
            setProgress(bundle.getFloat("progress"));
            setWrongAns(bundle.getFloat("goal"));

            // restore super state
            state = bundle.getParcelable("superState");
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int halfHeight = getHeight() / 2;
        int progressEndX = (int) (getWidth() * progress / 100f);

        // draw the unfilled portion of the bar
        Paint unfilledPaint = new Paint();
        unfilledPaint.setColor(unfilledSectionColor);
        unfilledPaint.setStrokeWidth(barThickness);
        canvas.drawLine(0, halfHeight, getWidth(), halfHeight, unfilledPaint);

        // draw Right Green portion of the bar
        Paint greenPain = new Paint();
        greenPain.setStyle(Paint.Style.FILL_AND_STROKE);
        greenPain.setStrokeWidth(barThickness);
        greenPain.setColor(goalReachedColor);
        canvas.drawLine(0, halfHeight, progressEndX, halfHeight, greenPain);

        // draw Wrong Red indicator
        Paint redPaint = new Paint();
        float indicatorPosition = (getWidth() * wrongAns / 100f);
        redPaint.setStrokeWidth(barThickness);
        redPaint.setColor(goalNotReachedColor);
        canvas.drawLine(progressEndX, halfHeight, totalReached == totaQue ? getWidth() : progressEndX + indicatorPosition, halfHeight, redPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int specHeight = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            // be exactly the given specHeight
            case MeasureSpec.EXACTLY:
                height = specHeight;
                break;

            // be at most the given specHeight
            case MeasureSpec.AT_MOST:
                height = (int) Math.min(goalIndicatorHeight, specHeight);
                break;

            // be whatever size you want
            case MeasureSpec.UNSPECIFIED:
            default:
                height = specHeight;
                break;
        }

        // must call this, otherwise the app will crash
        setMeasuredDimension(width, height);
    }

    public void setProgress(float progress, int totalQue) {
        this.totaQue = totalQue;
        setProgress(progress);
    }

    public void setProgress(final float progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setWrongAns(float wrongAns, int totalReached) {
        this.totalReached = totalReached;
        this.wrongAns = wrongAns;
        postInvalidate();
    }

    public void setWrongAns(float wrongAns) {
        this.wrongAns = wrongAns;
        postInvalidate();
    }

    public void setGoalIndicatorHeight(float goalIndicatorHeight) {
        this.goalIndicatorHeight = goalIndicatorHeight;
        postInvalidate();
    }

    public void setGoalReachedColor(int goalReachedColor) {
        this.goalReachedColor = goalReachedColor;
        postInvalidate();
    }

    public void setGoalNotReachedColor(int goalNotReachedColor) {
        this.goalNotReachedColor = goalNotReachedColor;
        postInvalidate();
    }

    public void setUnfilledSectionColor(int unfilledSectionColor) {
        this.unfilledSectionColor = unfilledSectionColor;
        postInvalidate();
    }

    public void setBarThickness(int barThickness) {
        this.barThickness = barThickness;
        postInvalidate();
    }
}