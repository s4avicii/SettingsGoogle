package com.google.android.settings.games;

import com.android.settings.SettingsActivity;

public class GameSettingsActivity extends SettingsActivity {
    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return GameSettings.class.getName().equals(str);
    }
}
