package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.window.C0444R;

public class WakeScreenDialogGesturePreferenceController extends AwareGesturePreferenceController {
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

    public WakeScreenDialogGesturePreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public CharSequence getGestureSummary() {
        return this.mContext.getText(isGestureEnabled() ? C0444R.string.gesture_setting_on : C0444R.string.gesture_setting_off);
    }

    private boolean isGestureEnabled() {
        if (!this.mFeatureProvider.isEnabled(this.mContext) || Settings.Secure.getInt(this.mContext.getContentResolver(), "doze_wake_screen_gesture", 1) != 1) {
            return false;
        }
        return true;
    }
}
