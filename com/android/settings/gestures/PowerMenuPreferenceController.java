package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;

public class PowerMenuPreferenceController extends BasePreferenceController {
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

    public PowerMenuPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        int powerButtonSettingValue = PowerMenuSettingsUtils.getPowerButtonSettingValue(this.mContext);
        if (powerButtonSettingValue == 5) {
            return this.mContext.getText(C0444R.string.power_menu_summary_long_press_for_assist_enabled);
        }
        if (powerButtonSettingValue == 1) {
            return this.mContext.getText(C0444R.string.f63x331c8baf);
        }
        return this.mContext.getText(C0444R.string.power_menu_summary_long_press_for_assist_disabled_no_action);
    }

    public int getAvailabilityStatus() {
        return isAssistInvocationAvailable() ? 0 : 3;
    }

    private boolean isAssistInvocationAvailable() {
        return this.mContext.getResources().getBoolean(17891690);
    }
}
