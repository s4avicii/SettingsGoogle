package com.google.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;

public class ShowMoreWarningsPreferenceController extends BasePreferenceController {
    static final String KEY_SECURITY_SHOW_MORE_WARNINGS = "security_show_more_warnings";
    private SecurityContentManager mSecurityContentManager;

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

    public ShowMoreWarningsPreferenceController(Context context) {
        super(context, KEY_SECURITY_SHOW_MORE_WARNINGS);
        this.mSecurityContentManager = SecurityContentManager.getInstance(context);
    }

    public void updateState(Preference preference) {
        ShowMoreWarningsPreference showMoreWarningsPreference = (ShowMoreWarningsPreference) preference;
        int securityWarningCount = this.mSecurityContentManager.getSecurityWarningCount();
        if (securityWarningCount > 1) {
            showMoreWarningsPreference.setTitle((CharSequence) this.mContext.getResources().getQuantityString(C0444R.plurals.security_settings_hub_show_warnings_preference, securityWarningCount, new Object[]{Integer.valueOf(securityWarningCount)}));
            showMoreWarningsPreference.setCardBackgroundColor(this.mContext.getColor(this.mSecurityContentManager.getPrimarySecurityWarning().getSecurityLevel().getAttentionLevel().getBackgroundColorResId()));
            showMoreWarningsPreference.setVisible(true);
            return;
        }
        showMoreWarningsPreference.setVisible(false);
    }
}
