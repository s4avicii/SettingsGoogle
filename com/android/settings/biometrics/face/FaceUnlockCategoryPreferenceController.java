package com.android.settings.biometrics.face;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;

public class FaceUnlockCategoryPreferenceController extends BasePreferenceController {
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

    public FaceUnlockCategoryPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return Utils.isMultipleBiometricsSupported(this.mContext) ? 3 : 0;
    }
}