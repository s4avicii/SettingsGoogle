package com.google.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.window.C0444R;
import com.android.settings.R$styleable;

public class AssistGestureTrainingProgressBar extends FrameLayout {
    private View mDoneView;
    private ProgressBar mProgressBar;
    private int mState;
    private TextView mTextView;

    public AssistGestureTrainingProgressBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public AssistGestureTrainingProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, C0444R.style.AssistGestureTrainingProgressBar);
    }

    public AssistGestureTrainingProgressBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet);
        this.mState = 0;
        LayoutInflater.from(context).inflate(C0444R.C0450layout.assist_gesture_training_progress_bar, this, true);
        this.mTextView = (TextView) findViewById(C0444R.C0448id.label);
        this.mProgressBar = (ProgressBar) findViewById(C0444R.C0448id.progress);
        this.mDoneView = findViewById(C0444R.C0448id.done);
        refreshViews();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.AssistGestureTrainingProgressBar, i, i2);
        this.mTextView.setText(obtainStyledAttributes.getText(0));
        obtainStyledAttributes.recycle();
    }

    public void setState(int i) {
        this.mState = i;
        refreshViews();
    }

    private void refreshViews() {
        int i = 0;
        this.mTextView.setVisibility(this.mState != 2 ? 0 : 8);
        this.mProgressBar.setVisibility(this.mState == 1 ? 0 : 8);
        View view = this.mDoneView;
        if (this.mState != 2) {
            i = 8;
        }
        view.setVisibility(i);
    }

    public void setText(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
    }
}
