package com.google.android.settings.fuelgauge.reversecharging;

import android.os.Bundle;
import androidx.window.C0444R;
import com.android.settings.core.SettingsBaseActivity;
import com.android.settings.core.SubSettingLauncher;

public class ReverseChargingTrampoline extends SettingsBaseActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startFragmentIfNecessary(ReverseChargingManager.getInstance(this));
    }

    /* access modifiers changed from: package-private */
    public void startFragmentIfNecessary(ReverseChargingManager reverseChargingManager) {
        if (reverseChargingManager.isSupportedReverseCharging()) {
            new SubSettingLauncher(getApplicationContext()).setDestination(ReverseChargingDashboardFragment.class.getName()).setTitleRes(C0444R.string.reverse_charging_title).addFlags(268435456).setSourceMetricsCategory(1842).launch();
        }
        finish();
    }
}
