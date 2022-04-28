package com.google.android.settings.gestures.columbus;

import com.android.settings.SettingsActivity;

public class ColumbusSettingsActivity extends SettingsActivity {
    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return ColumbusSettings.class.getName().equals(str);
    }
}
