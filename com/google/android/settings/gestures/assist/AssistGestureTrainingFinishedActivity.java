package com.google.android.settings.gestures.assist;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import androidx.window.C0444R;
import com.android.settings.SetupWizardUtils;
import com.google.android.settings.gestures.assist.AssistGestureHelper;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;

public class AssistGestureTrainingFinishedActivity extends AssistGestureTrainingSliderBase {
    private boolean mAccessibilityAnnounced;
    private View mAssistGestureCheck;
    private View mAssistGestureIllustration;
    private GlifLayout mLayout;

    public int getMetricsCategory() {
        return 993;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        setContentView((int) C0444R.C0450layout.assist_gesture_training_finished_activity);
        this.mLayout = (GlifLayout) findViewById(C0444R.C0448id.layout);
        super.onCreate(bundle);
        setShouldCheckForNoProgress(false);
        FooterBarMixin footerBarMixin = (FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class);
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.assist_gesture_enrollment_settings).setListener(new AssistGestureTrainingFinishedActivity$$ExternalSyntheticLambda1(this)).setButtonType(0).setTheme(C0444R.style.SudGlifButton_Secondary).build());
        FooterButton secondaryButton = footerBarMixin.getSecondaryButton();
        if (flowTypeDeferredSetup() || flowTypeSetup()) {
            secondaryButton.setVisibility(4);
        }
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.done).setListener(new AssistGestureTrainingFinishedActivity$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
        FooterButton primaryButton = footerBarMixin.getPrimaryButton();
        if (flowTypeDeferredSetup() || flowTypeSetup()) {
            primaryButton.setText(this, C0444R.string.next_label);
        } else if (flowTypeSettingsSuggestion()) {
            primaryButton.setText(this, C0444R.string.done);
        } else if (flowTypeAccidentalTrigger()) {
            primaryButton.setText(this, C0444R.string.assist_gesture_enrollment_continue_to_assistant);
        }
        this.mAssistGestureCheck = findViewById(C0444R.C0448id.assist_gesture_training_check);
        this.mAssistGestureIllustration = findViewById(C0444R.C0448id.assist_gesture_training_illustration);
        fadeOutCheckAfterDelay();
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        if (flowTypeDeferredSetup() || flowTypeSettingsSuggestion() || flowTypeSetup()) {
            setResult(-1);
            this.mAssistGestureHelper.setListener((AssistGestureHelper.GestureListener) null);
            finishAndRemoveTask();
        } else if (flowTypeAccidentalTrigger()) {
            handleDoneAndLaunch();
        }
    }

    /* access modifiers changed from: private */
    public void onSettingsButtonClicked(View view) {
        launchAssistGestureSettings();
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        super.onProgressChanged(seekBar, i, z);
    }

    /* access modifiers changed from: protected */
    public void showMessage(int i, String str) {
        if (this.mAssistGestureCheck.getVisibility() == 4) {
            super.showMessage(i, str);
        }
    }

    /* access modifiers changed from: protected */
    public void handleGestureDetected() {
        this.mErrorView.setVisibility(4);
        this.mAssistGestureCheck.animate().cancel();
        this.mAssistGestureCheck.setAlpha(1.0f);
        this.mAssistGestureCheck.setVisibility(0);
        if (!this.mAccessibilityAnnounced) {
            this.mLayout.announceForAccessibility(getApplicationContext().getResources().getString(C0444R.string.accessibility_assist_gesture_complete_or_keep_adjusting));
            this.mAccessibilityAnnounced = true;
        }
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(5);
        fadeOutCheckAfterDelay();
        fadeIndicators();
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(5);
        this.mHandler.obtainMessage(6, this.mAssistGestureCheck).sendToTarget();
        this.mHandler.obtainMessage(7, this.mAssistGestureIllustration).sendToTarget();
    }

    private void fadeOutCheckAfterDelay() {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(5, this.mAssistGestureCheck), 1000);
    }
}
