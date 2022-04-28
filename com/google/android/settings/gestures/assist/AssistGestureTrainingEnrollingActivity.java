package com.google.android.settings.gestures.assist;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import androidx.window.C0444R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;

public class AssistGestureTrainingEnrollingActivity extends AssistGestureTrainingSliderBase {
    public int getMetricsCategory() {
        return 992;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        setContentView((int) C0444R.C0450layout.assist_gesture_training_enrolling_activity);
        super.onCreate(bundle);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(3, 100);
        ((LinearLayout) findViewById(C0444R.C0448id.content_container)).setLayoutTransition(layoutTransition);
        ((FooterBarMixin) ((GlifLayout) findViewById(C0444R.C0448id.layout)).getMixin(FooterBarMixin.class)).setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.assist_gesture_enrollment_do_it_later).setListener(new AssistGestureTrainingEnrollingActivity$$ExternalSyntheticLambda0(this)).setButtonType(2).setTheme(C0444R.style.SudGlifButton_Secondary).build());
    }

    /* access modifiers changed from: protected */
    public void handleGestureDetected() {
        clearIndicators();
        this.mErrorView.setVisibility(4);
        Settings.Secure.putInt(getContentResolver(), "assist_gesture_setup_complete", 1);
        startFinishedActivity();
        finishAndRemoveTask();
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        setResult(101);
        finishAndRemoveTask();
    }

    private void startFinishedActivity() {
        Intent intent = new Intent(this, AssistGestureTrainingFinishedActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        intent.addFlags(33554432);
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivity(intent);
    }
}
