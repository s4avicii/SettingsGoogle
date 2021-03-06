package com.android.settings.fuelgauge;

import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class RestrictedPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    String KEY_RESTRICTED_PREF = "restricted_pref";
    BatteryOptimizeUtils mBatteryOptimizeUtils;

    public boolean isAvailable() {
        return true;
    }

    public RestrictedPreferenceController(Context context, int i, String str) {
        super(context);
        this.mBatteryOptimizeUtils = new BatteryOptimizeUtils(context, i, str);
    }

    public void updateState(Preference preference) {
        if (!this.mBatteryOptimizeUtils.isValidPackageName()) {
            Log.d("RESTRICTED_PREF", "invalid package name, disable pref");
            preference.setEnabled(false);
            return;
        }
        preference.setEnabled(true);
        if (this.mBatteryOptimizeUtils.isSystemOrDefaultApp()) {
            Log.d("RESTRICTED_PREF", "is system or default app, disable pref");
            ((SelectorWithWidgetPreference) preference).setChecked(false);
            preference.setEnabled(false);
        } else if (this.mBatteryOptimizeUtils.getAppOptimizationMode() == 1) {
            Log.d("RESTRICTED_PREF", "is restricted states");
            ((SelectorWithWidgetPreference) preference).setChecked(true);
        } else {
            ((SelectorWithWidgetPreference) preference).setChecked(false);
        }
    }

    public String getPreferenceKey() {
        return this.KEY_RESTRICTED_PREF;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        return getPreferenceKey().equals(preference.getKey());
    }
}
