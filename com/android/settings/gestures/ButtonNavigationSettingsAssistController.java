package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;

public class ButtonNavigationSettingsAssistController extends TogglePreferenceController {
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_system;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ButtonNavigationSettingsAssistController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "assist_long_press_home_enabled", this.mContext.getResources().getBoolean(17891368) ? 1 : 0) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "assist_long_press_home_enabled", z ? 1 : 0);
    }

    public int getAvailabilityStatus() {
        return (SystemNavigationPreferenceController.isOverlayPackageAvailable(this.mContext, "com.android.internal.systemui.navbar.twobutton") || SystemNavigationPreferenceController.isOverlayPackageAvailable(this.mContext, "com.android.internal.systemui.navbar.threebutton")) ? 0 : 3;
    }
}
