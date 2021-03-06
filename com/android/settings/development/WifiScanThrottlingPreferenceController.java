package com.android.settings.development;

import android.content.Context;
import android.net.wifi.WifiManager;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class WifiScanThrottlingPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    private final WifiManager mWifiManager;

    public String getPreferenceKey() {
        return "wifi_scan_throttling";
    }

    public WifiScanThrottlingPreferenceController(Context context) {
        super(context);
        this.mWifiManager = (WifiManager) context.getSystemService(WifiManager.class);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        this.mWifiManager.setScanThrottleEnabled(((Boolean) obj).booleanValue());
        return true;
    }

    public void updateState(Preference preference) {
        ((SwitchPreference) this.mPreference).setChecked(this.mWifiManager.isScanThrottleEnabled());
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        this.mWifiManager.setScanThrottleEnabled(true);
        ((SwitchPreference) this.mPreference).setChecked(true);
    }
}
