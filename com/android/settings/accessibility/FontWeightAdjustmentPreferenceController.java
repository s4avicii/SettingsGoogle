package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.window.C0444R;
import com.android.settings.accessibility.TextReadingResetController;
import com.android.settings.core.TogglePreferenceController;

public class FontWeightAdjustmentPreferenceController extends TogglePreferenceController implements TextReadingResetController.ResetStateListener {
    static final int BOLD_TEXT_ADJUSTMENT = 300;

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

    public int getSliceHighlightMenuRes() {
        return C0444R.string.menu_key_accessibility;
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

    public FontWeightAdjustmentPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "font_weight_adjustment", 0) == BOLD_TEXT_ADJUSTMENT;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "font_weight_adjustment", z ? BOLD_TEXT_ADJUSTMENT : 0);
    }

    public void resetState() {
        setChecked(false);
    }
}
