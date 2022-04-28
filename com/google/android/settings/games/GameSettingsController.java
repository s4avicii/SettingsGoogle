package com.google.android.settings.games;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import com.android.settings.core.BasePreferenceController;

public class GameSettingsController extends BasePreferenceController {
    private static final String TAG = "GameSettingsController";

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

    public GameSettingsController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        PackageManager packageManager = this.mContext.getPackageManager();
        if (!Build.IS_DEBUGGABLE) {
            return (packageManager == null || !packageManager.hasSystemFeature("com.google.android.feature.GAME_OVERLAY")) ? 3 : 0;
        }
        return 0;
    }
}
