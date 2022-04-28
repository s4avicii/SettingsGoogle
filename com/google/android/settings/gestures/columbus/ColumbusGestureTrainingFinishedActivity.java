package com.google.android.settings.gestures.columbus;

import android.app.ActivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.window.C0444R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class ColumbusGestureTrainingFinishedActivity extends ColumbusGestureTrainingBase {
    public int getMetricsCategory() {
        return 1750;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setContentView((int) C0444R.C0450layout.columbus_gesture_training_finished_activity);
        GlifLayout glifLayout = (GlifLayout) findViewById(C0444R.C0448id.layout);
        super.onCreate(bundle);
        glifLayout.setHeaderText((int) C0444R.string.columbus_gesture_training_finished_title);
        glifLayout.setDescriptionText((int) C0444R.string.columbus_gesture_training_finished_text);
        FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.columbus_gesture_enrollment_settings).setListener(new C1817xe4e8ae2b(this)).setButtonType(0).setTheme(C0444R.style.SudGlifButton_Secondary).build());
        FooterButton secondaryButton = footerBarMixin.getSecondaryButton();
        if (flowTypeDeferredSetup() || flowTypeSetup()) {
            secondaryButton.setVisibility(4);
        }
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.done).setListener(new C1818xe4e8ae2c(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
        FooterButton primaryButton = footerBarMixin.getPrimaryButton();
        if (flowTypeDeferredSetup() || flowTypeSetup()) {
            primaryButton.setText(this, C0444R.string.next_label);
        } else if (flowTypeSettingsSuggestion()) {
            primaryButton.setText(this, C0444R.string.done);
        } else if (flowTypeAccidentalTrigger()) {
            primaryButton.setText(this, C0444R.string.columbus_gesture_enrollment_complete);
        }
        Settings.Secure.putIntForUser(getContentResolver(), "columbus_suw_complete", 1, ActivityManager.getCurrentUser());
        setEnableColumbusOnPause();
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        handleDone();
    }

    /* access modifiers changed from: private */
    public void onSettingsButtonClicked(View view) {
        launchColumbusGestureSettings(getMetricsCategory());
    }
}
