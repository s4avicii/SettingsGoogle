package com.google.android.settings.gestures.columbus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.window.C0444R;
import com.airbnb.lottie.LottieAnimationView;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class ColumbusGestureTrainingEnrollingActivity extends ColumbusGestureTrainingBase {
    private LottieAnimationView mAnimation;
    private boolean mFirstGestureDetected;
    private final Handler mHandler = new Handler(Looper.myLooper());
    private ColumbusEnrollingIllustration mIllustration;
    private GlifLayout mLayout;

    public int getMetricsCategory() {
        return 1749;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setContentView((int) C0444R.C0450layout.columbus_gesture_training_enrolling_activity);
        super.onCreate(bundle);
        GlifLayout glifLayout = (GlifLayout) findViewById(C0444R.C0448id.layout);
        this.mLayout = glifLayout;
        this.mAnimation = (LottieAnimationView) glifLayout.findViewById(C0444R.C0448id.animation);
        this.mIllustration = (ColumbusEnrollingIllustration) this.mLayout.findViewById(C0444R.C0448id.columbus_gesture_illustration);
        this.mLayout.setDescriptionText((int) C0444R.string.columbus_gesture_training_enrolling_text);
        ((FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class)).setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.columbus_gesture_enrollment_do_it_later).setListener(new C1812x6af21a1f(this)).setButtonType(2).setTheme(C0444R.style.SudGlifButton_Secondary).build());
    }

    public void onTrigger() {
        if (this.mFirstGestureDetected) {
            this.mHandler.post(new C1815x6af21a22(this));
            return;
        }
        this.mFirstGestureDetected = true;
        this.mHandler.post(new C1816x6af21a23(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrigger$1() {
        this.mLayout.setHeaderText((int) C0444R.string.columbus_gesture_training_enrolling_second_gesture_title);
        this.mLayout.setDescriptionText((int) C0444R.string.columbus_gesture_training_enrolling_second_gesture_text);
        ((FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class)).setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.wizard_next).setListener(new C1813x6af21a20(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
        this.mIllustration.setGestureCount(2, new C1814x6af21a21(this));
        this.mLayout.requestAccessibilityFocus();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrigger$0() {
        this.mAnimation.cancelAnimation();
        this.mAnimation.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrigger$2() {
        this.mLayout.setHeaderText((int) C0444R.string.columbus_gesture_training_enrolling_first_gesture_title);
        this.mLayout.setDescriptionText((int) C0444R.string.columbus_gesture_training_enrolling_first_gesture_text);
        this.mIllustration.setGestureCount(1, (Runnable) null);
        this.mLayout.requestAccessibilityFocus();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            setResult(i2, intent);
            finishAndRemoveTask();
        }
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        startActionActivity();
        finishAndRemoveTask();
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        setResult(101);
        finishAndRemoveTask();
    }

    private void startActionActivity() {
        Intent intent = new Intent(this, ColumbusGestureTrainingActionActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 1);
    }
}
