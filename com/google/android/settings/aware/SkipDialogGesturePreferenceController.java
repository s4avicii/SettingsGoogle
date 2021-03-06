package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.window.C0444R;

public class SkipDialogGesturePreferenceController extends AwareGesturePreferenceController {
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

    public SkipDialogGesturePreferenceController(Context context, String str) {
        super(context, str);
    }

    private boolean isSkipGestureEnabled() {
        if (!this.mHelper.isEnabled() || Settings.Secure.getInt(this.mContext.getContentResolver(), "skip_gesture", 1) != 1) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public CharSequence getGestureSummary() {
        return this.mContext.getText(isSkipGestureEnabled() ? C0444R.string.gesture_skip_on_summary : C0444R.string.gesture_setting_off);
    }
}
