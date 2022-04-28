package com.android.settings.fuelgauge.batterytip.actions;

import android.content.Intent;
import com.android.settings.SettingsActivity;
import com.android.settings.overlay.FeatureFactory;

public class BatteryDefenderAction extends BatteryTipAction {
    private SettingsActivity mSettingsActivity;

    public BatteryDefenderAction(SettingsActivity settingsActivity) {
        super(settingsActivity.getApplicationContext());
        this.mSettingsActivity = settingsActivity;
    }

    public void handlePositiveAction(int i) {
        Intent resumeChargeIntent = FeatureFactory.getFactory(this.mContext).getPowerUsageFeatureProvider(this.mContext).getResumeChargeIntent();
        if (resumeChargeIntent != null) {
            this.mContext.sendBroadcast(resumeChargeIntent);
        }
    }
}
