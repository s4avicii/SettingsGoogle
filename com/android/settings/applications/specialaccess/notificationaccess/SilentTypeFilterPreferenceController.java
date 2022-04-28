package com.android.settings.applications.specialaccess.notificationaccess;

import android.content.Context;
import android.content.IntentFilter;

public class SilentTypeFilterPreferenceController extends TypeFilterPreferenceController {
    private static final String TAG = "SilentFilterPrefCntlr";

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

    /* access modifiers changed from: protected */
    public int getType() {
        return 4;
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

    public SilentTypeFilterPreferenceController(Context context, String str) {
        super(context, str);
    }
}