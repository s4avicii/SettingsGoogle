package com.android.settings.notification.zen;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

public class ZenModeConversationsPreferenceController extends AbstractZenModePreferenceController {
    private final ZenModeBackend mBackend;
    private Preference mPreference;

    public boolean isAvailable() {
        return true;
    }

    public String getPreferenceKey() {
        return this.KEY;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(this.KEY);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        int zenMode = getZenMode();
        if (zenMode == 2 || zenMode == 3) {
            this.mPreference.setEnabled(false);
            this.mPreference.setSummary(this.mBackend.getAlarmsTotalSilencePeopleSummary(256));
            return;
        }
        preference.setEnabled(true);
        preference.setSummary(this.mBackend.getConversationSummary());
    }
}
