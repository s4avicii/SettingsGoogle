package com.google.android.settings.security;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.security.ChangeScreenLockPreferenceController;

public class ChangeScreenLockGooglePreferenceController extends ChangeScreenLockPreferenceController {
    private final SecurityContentManager mSecurityContentManager;

    public String getPreferenceKey() {
        return "securityhub_unlock_set_or_change";
    }

    public ChangeScreenLockGooglePreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment) {
        super(context, settingsPreferenceFragment);
        this.mSecurityContentManager = SecurityContentManager.getInstance(context);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setIcon(this.mSecurityContentManager.getScreenLockSecurityLevel(this.mLockPatternUtils.isSecure(this.mUserId)).getEntryIconResId());
        preference.setOrder(this.mSecurityContentManager.getScreenLockOrder());
    }
}
