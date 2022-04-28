package com.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.CrossProfileApps;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;

public class SecurityAdvancedSettingsController extends BasePreferenceController {
    private final CrossProfileApps mCrossProfileApps;

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

    public SecurityAdvancedSettingsController(Context context, String str) {
        super(context, str);
        this.mCrossProfileApps = (CrossProfileApps) context.getSystemService(CrossProfileApps.class);
    }

    public CharSequence getSummary() {
        if (isWorkProfilePresent()) {
            return this.mContext.getResources().getString(C0444R.string.security_advanced_settings_work_profile_settings_summary);
        }
        return this.mContext.getResources().getString(C0444R.string.security_advanced_settings_no_work_profile_settings_summary);
    }

    private boolean isWorkProfilePresent() {
        return !this.mCrossProfileApps.getTargetUserProfiles().isEmpty();
    }
}
