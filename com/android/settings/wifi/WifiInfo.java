package com.android.settings.wifi;

import android.os.Bundle;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;

public class WifiInfo extends SettingsPreferenceFragment {
    public int getMetricsCategory() {
        return 89;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(C0444R.xml.testing_wifi_settings);
    }
}
