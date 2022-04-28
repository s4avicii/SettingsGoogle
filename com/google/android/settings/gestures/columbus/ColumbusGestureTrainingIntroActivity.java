package com.google.android.settings.gestures.columbus;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.window.C0444R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;

public class ColumbusGestureTrainingIntroActivity extends ColumbusGestureTrainingBase {
    private static final String FROM_ACCIDENTAL_TRIGGER_CLASS = "com.google.android.settings.gestures.columbus.ColumbusGestureTrainingIntroActivity";

    public int getMetricsCategory() {
        return 1748;
    }

    public void onTrigger() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        super.onCreate(bundle);
        if (!ColumbusPreferenceController.isColumbusSupported(this)) {
            finish();
            return;
        }
        if (Settings.Secure.getIntForUser(getContentResolver(), "columbus_suw_complete", 0, ActivityManager.getCurrentUser()) != 0) {
            launchColumbusGestureSettings(0);
            finish();
            return;
        }
        setContentView((int) C0444R.C0450layout.columbus_gesture_training_intro_activity);
        GlifLayout glifLayout = (GlifLayout) findViewById(C0444R.C0448id.layout);
        glifLayout.setDescriptionText((int) C0444R.string.columbus_gesture_training_intro_text_suw);
        FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.columbus_gesture_enrollment_try_it).setListener(new ColumbusGestureTrainingIntroActivity$$ExternalSyntheticLambda1(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.columbus_gesture_enrollment_do_it_later).setListener(new ColumbusGestureTrainingIntroActivity$$ExternalSyntheticLambda0(this)).setButtonType(2).setTheme(C0444R.style.SudGlifButton_Secondary).build());
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
        startEnrollingActivity();
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        setResult(101);
        finishAndRemoveTask();
    }

    private void startEnrollingActivity() {
        Intent intent = new Intent(this, ColumbusGestureTrainingEnrollingActivity.class);
        intent.putExtra("launched_from", getFlowType());
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 1);
    }

    private String getFlowType() {
        Intent intent = getIntent();
        if (WizardManagerHelper.isSetupWizardIntent(intent)) {
            return "setup";
        }
        if (WizardManagerHelper.isDeferredSetupWizard(intent)) {
            return "deferred_setup";
        }
        if ("com.google.android.settings.gestures.ColumbusGestureSuggestion".contentEquals(intent.getComponent().getClassName())) {
            return "settings_suggestion";
        }
        if (FROM_ACCIDENTAL_TRIGGER_CLASS.contentEquals(intent.getComponent().getClassName())) {
            return "accidental_trigger";
        }
        return null;
    }
}
