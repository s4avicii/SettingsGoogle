package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.window.C0444R;

public class AwareTapPreferenceController extends AwareBasePreferenceController {
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
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

    public AwareTapPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        return this.mContext.getText(isTapGestureEnabled() ? C0444R.string.gesture_tap_on_summary : C0444R.string.gesture_setting_off);
    }

    public int getAvailabilityStatus() {
        if (AwareHelper.isTapAvailableOnTheDevice()) {
            return super.getAvailabilityStatus();
        }
        return 3;
    }

    private boolean isTapGestureEnabled() {
        return this.mFeatureProvider.isEnabled(this.mContext) && Settings.Secure.getInt(this.mContext.getContentResolver(), "tap_gesture", 0) == 1;
    }
}
