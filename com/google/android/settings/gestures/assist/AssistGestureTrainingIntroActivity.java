package com.google.android.settings.gestures.assist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.window.C0444R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;

public class AssistGestureTrainingIntroActivity extends AssistGestureTrainingBase {
    private static final String FROM_ACCIDENTAL_TRIGGER_CLASS = "com.google.android.settings.gestures.assist.AssistGestureTrainingIntroActivity";

    public int getMetricsCategory() {
        return 991;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        super.onCreate(bundle);
        ThemeHelper.trySetDynamicColor(this);
        setContentView((int) C0444R.C0450layout.assist_gesture_training_intro_activity);
        FooterBarMixin footerBarMixin = (FooterBarMixin) ((GlifLayout) findViewById(C0444R.C0448id.layout)).getMixin(FooterBarMixin.class);
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.assist_gesture_enrollment_do_it_later).setListener(new AssistGestureTrainingIntroActivity$$ExternalSyntheticLambda1(this)).setButtonType(2).setTheme(C0444R.style.SudGlifButton_Secondary).build());
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.wizard_next).setListener(new AssistGestureTrainingIntroActivity$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
        FooterButton secondaryButton = footerBarMixin.getSecondaryButton();
        if ("accidental_trigger".contentEquals(getFlowType())) {
            secondaryButton.setText(this, C0444R.string.assist_gesture_enrollment_settings);
        } else {
            secondaryButton.setText(this, C0444R.string.assist_gesture_enrollment_do_it_later);
        }
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        startEnrollingActivity();
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        if ("accidental_trigger".contentEquals(getFlowType())) {
            launchAssistGestureSettings();
            return;
        }
        setResult(101);
        finishAndRemoveTask();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 != 0) {
            setResult(i2, intent);
            finishAndRemoveTask();
        }
    }

    private String getFlowType() {
        Intent intent = getIntent();
        if (WizardManagerHelper.isSetupWizardIntent(intent)) {
            return "setup";
        }
        if (WizardManagerHelper.isDeferredSetupWizard(intent)) {
            return "deferred_setup";
        }
        if ("com.google.android.settings.gestures.AssistGestureSuggestion".contentEquals(intent.getComponent().getClassName())) {
            return "settings_suggestion";
        }
        if (FROM_ACCIDENTAL_TRIGGER_CLASS.contentEquals(intent.getComponent().getClassName())) {
            return "accidental_trigger";
        }
        return null;
    }

    private void startEnrollingActivity() {
        Intent intent = new Intent(this, AssistGestureTrainingEnrollingActivity.class);
        intent.putExtra("launched_from", getFlowType());
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 1);
    }

    public void onGestureProgress(float f, int i) {
        super.onGestureProgress(f, i);
    }

    public void onGestureDetected() {
        clearIndicators();
        startEnrollingActivity();
    }
}
