package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import androidx.window.C0444R;

public class SilenceGestureFooterPreferenceController extends AwareFooterPreferenceController {
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

    public int getText() {
        return C0444R.string.gesture_aware_footer;
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

    public /* bridge */ /* synthetic */ int getAvailabilityStatus() {
        return super.getAvailabilityStatus();
    }

    public /* bridge */ /* synthetic */ CharSequence getSummary() {
        return super.getSummary();
    }

    public SilenceGestureFooterPreferenceController(Context context, String str) {
        super(context, str);
    }
}
