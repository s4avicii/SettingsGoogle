package com.android.settings.notification;

import android.content.Context;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class DockingSoundPreferenceController extends SettingPrefController {
    public DockingSoundPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(1, "docking_sounds", "dock_sounds_enabled", 1, new int[0]) {
            public boolean isApplicable(Context context) {
                return context.getResources().getBoolean(C0444R.bool.has_dock_settings);
            }
        };
    }
}
