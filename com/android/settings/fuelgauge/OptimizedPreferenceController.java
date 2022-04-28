package com.android.settings.fuelgauge;

import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class OptimizedPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    String KEY_OPTIMIZED_PREF = "optimized_pref";
    BatteryOptimizeUtils mBatteryOptimizeUtils;

    public boolean isAvailable() {
        return true;
    }

    public OptimizedPreferenceController(Context context, int i, String str) {
        super(context);
        this.mBatteryOptimizeUtils = new BatteryOptimizeUtils(context, i, str);
    }

    public void updateState(Preference preference) {
        if (!this.mBatteryOptimizeUtils.isValidPackageName()) {
            Log.d("OPTIMIZED_PREF", "invalid package name, optimized states only");
            preference.setEnabled(true);
            ((SelectorWithWidgetPreference) preference).setChecked(true);
        } else if (this.mBatteryOptimizeUtils.isSystemOrDefaultApp()) {
            Log.d("OPTIMIZED_PREF", "is system or default app, disable pref");
            ((SelectorWithWidgetPreference) preference).setChecked(false);
            preference.setEnabled(false);
        } else if (this.mBatteryOptimizeUtils.getAppOptimizationMode() == 3) {
            Log.d("OPTIMIZED_PREF", "is optimized states");
            ((SelectorWithWidgetPreference) preference).setChecked(true);
        } else {
            ((SelectorWithWidgetPreference) preference).setChecked(false);
        }
    }

    public String getPreferenceKey() {
        return this.KEY_OPTIMIZED_PREF;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        return getPreferenceKey().equals(preference.getKey());
    }
}
