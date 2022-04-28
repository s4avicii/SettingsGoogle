package com.google.android.settings.gestures.columbus;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;

public class ColumbusPreferenceController extends BasePreferenceController {
    static final String FEATURE_QUICK_TAP = "com.google.android.feature.QUICK_TAP";

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

    public ColumbusPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        if (!isColumbusEnabled(this.mContext)) {
            return this.mContext.getText(C0444R.string.gesture_setting_off);
        }
        CharSequence text = this.mContext.getText(C0444R.string.gesture_setting_on);
        String columbusAction = ColumbusActionsPreferenceController.getColumbusAction(this.mContext);
        return this.mContext.getString(C0444R.string.columbus_summary, new Object[]{text, columbusAction});
    }

    static boolean isColumbusSupported(Context context) {
        return context.getPackageManager().hasSystemFeature(FEATURE_QUICK_TAP);
    }

    static boolean isColumbusEnabled(Context context) {
        if (Settings.Secure.getIntForUser(context.getContentResolver(), "columbus_enabled", 0, ActivityManager.getCurrentUser()) != 0) {
            return true;
        }
        return false;
    }
}
