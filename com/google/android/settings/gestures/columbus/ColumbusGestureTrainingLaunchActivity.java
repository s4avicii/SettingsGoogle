package com.google.android.settings.gestures.columbus;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.window.C0444R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import java.util.Comparator;
import java.util.List;

public class ColumbusGestureTrainingLaunchActivity extends ColumbusGestureTrainingBase {
    private RadioGroup mRadioGroup;

    public int getMetricsCategory() {
        return 1759;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setContentView((int) C0444R.C0450layout.columbus_gesture_training_launch_activity);
        super.onCreate(bundle);
        this.mRadioGroup = (RadioGroup) findViewById(C0444R.C0448id.apps);
        List<LauncherActivityInfo> activityList = ((LauncherApps) getSystemService(LauncherApps.class)).getActivityList((String) null, UserHandle.of(ActivityManager.getCurrentUser()));
        activityList.sort(Comparator.comparing(ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda2.INSTANCE));
        LayoutInflater from = LayoutInflater.from(this.mRadioGroup.getContext());
        int dimensionPixelSize = getResources().getDimensionPixelSize(C0444R.dimen.columbus_app_icon_size);
        for (LauncherActivityInfo next : activityList) {
            ColumbusRadioButton columbusRadioButton = (ColumbusRadioButton) from.inflate(C0444R.C0450layout.columbus_app_list_item, this.mRadioGroup, false);
            columbusRadioButton.setText(next.getLabel());
            Drawable icon = next.getIcon(0);
            icon.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
            columbusRadioButton.setCompoundDrawablesRelative(icon, (Drawable) null, (Drawable) null, (Drawable) null);
            columbusRadioButton.setSecureValue(next.getComponentName().flattenToString());
            this.mRadioGroup.addView(columbusRadioButton);
        }
        GlifLayout glifLayout = (GlifLayout) findViewById(C0444R.C0448id.layout);
        glifLayout.setDescriptionText((int) C0444R.string.columbus_gesture_training_launch_text);
        FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.wizard_next).setListener(new ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(C0444R.style.SudGlifButton_Primary).build());
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText((int) C0444R.string.columbus_gesture_enrollment_do_it_later).setListener(new ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda1(this)).setButtonType(2).setTheme(C0444R.style.SudGlifButton_Secondary).build());
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
        String str;
        ColumbusRadioButton columbusRadioButton = (ColumbusRadioButton) this.mRadioGroup.findViewById(this.mRadioGroup.getCheckedRadioButtonId());
        if (columbusRadioButton == null) {
            str = null;
        } else {
            str = columbusRadioButton.getSecureValue();
        }
        if (str == null) {
            Toast.makeText(this, C0444R.string.columbus_gesture_training_launch_no_selection_error, 0).show();
            return;
        }
        Settings.Secure.putStringForUser(getContentResolver(), "columbus_launch_app", str, ActivityManager.getCurrentUser());
        Settings.Secure.putStringForUser(getContentResolver(), "columbus_launch_app_shortcut", str, ActivityManager.getCurrentUser());
        startFinishedActivity();
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        setResult(101);
        finishAndRemoveTask();
    }

    private void startFinishedActivity() {
        Intent intent = new Intent(this, ColumbusGestureTrainingFinishedActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 1);
    }
}
