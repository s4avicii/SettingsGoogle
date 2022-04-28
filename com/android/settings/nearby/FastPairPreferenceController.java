package com.android.settings.nearby;

import android.content.Context;
import android.content.IntentFilter;
import androidx.lifecycle.LifecycleObserver;
import com.android.settings.core.BasePreferenceController;

public class FastPairPreferenceController extends BasePreferenceController implements LifecycleObserver {
    public static final String KEY_FAST_PAIR_SETTINGS = "connected_device_fast_pair";
    public static final String TAG = "FastPairPrefController";

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

    public FastPairPreferenceController(Context context) {
        super(context, KEY_FAST_PAIR_SETTINGS);
    }
}
