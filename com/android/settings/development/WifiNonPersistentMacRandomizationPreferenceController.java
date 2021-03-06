package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class WifiNonPersistentMacRandomizationPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "wifi_non_persistent_mac_randomization";
    }

    public WifiNonPersistentMacRandomizationPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "non_persistent_mac_randomization_force_enabled", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = false;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "non_persistent_mac_randomization_force_enabled", 0) == 1) {
            z = true;
        }
        ((SwitchPreference) this.mPreference).setChecked(z);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        Settings.Global.putInt(this.mContext.getContentResolver(), "non_persistent_mac_randomization_force_enabled", 0);
        ((SwitchPreference) this.mPreference).setChecked(false);
    }
}
