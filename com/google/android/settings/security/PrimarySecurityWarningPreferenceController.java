package com.google.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.BasePreferenceController;

public class PrimarySecurityWarningPreferenceController extends BasePreferenceController {
    private SettingsPreferenceFragment mHost;
    private final SecurityContentManager mSecurityContentManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 1;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getMetricsCategory() {
        return 1884;
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PrimarySecurityWarningPreferenceController(Context context, String str) {
        super(context, str);
        this.mSecurityContentManager = SecurityContentManager.getInstance(context);
    }

    public void init(SettingsPreferenceFragment settingsPreferenceFragment) {
        this.mHost = settingsPreferenceFragment;
    }

    public void updateState(Preference preference) {
        SecurityWarningPreference securityWarningPreference = (SecurityWarningPreference) preference;
        SecurityWarning primarySecurityWarning = this.mSecurityContentManager.getPrimarySecurityWarning();
        if (primarySecurityWarning != null) {
            securityWarningPreference.setSecurityWarning(primarySecurityWarning, this.mHost);
            securityWarningPreference.setVisible(true);
            return;
        }
        securityWarningPreference.setVisible(false);
    }
}
