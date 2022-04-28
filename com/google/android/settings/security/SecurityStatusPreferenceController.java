package com.google.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.widget.LayoutPreference;
import com.google.android.settings.security.SecurityContentManager;

public class SecurityStatusPreferenceController extends BasePreferenceController {
    private SecurityContentManager mSecurityContentManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
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

    public SecurityStatusPreferenceController(Context context, String str) {
        super(context, str);
        this.mSecurityContentManager = SecurityContentManager.getInstance(context);
    }

    public void updateState(Preference preference) {
        SecurityContentManager.OverallStatus overallStatus = this.mSecurityContentManager.getOverallStatus();
        if (overallStatus != null) {
            LayoutPreference layoutPreference = (LayoutPreference) preference;
            ((ImageView) layoutPreference.findViewById(C0444R.C0448id.status_image)).setImageResource(overallStatus.getStatusSecurityLevel().getImageResId());
            ((TextView) layoutPreference.findViewById(C0444R.C0448id.status_title)).setText(overallStatus.getTitle());
            ((TextView) layoutPreference.findViewById(C0444R.C0448id.status_summary)).setText(overallStatus.getSummary());
        }
    }
}
