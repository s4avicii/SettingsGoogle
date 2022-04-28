package com.google.android.settings.fuelgauge;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;

public class AdaptiveChargingPreferenceController extends TogglePreferenceController {
    @VisibleForTesting
    AdaptiveChargingManager mAdaptiveChargingManager;
    private boolean mChecked;

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
        return C0444R.string.menu_key_battery;
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

    public AdaptiveChargingPreferenceController(Context context, String str) {
        super(context, str);
        this.mAdaptiveChargingManager = new AdaptiveChargingManager(context);
    }

    public int getAvailabilityStatus() {
        return this.mAdaptiveChargingManager.isAvailable() ? 0 : 3;
    }

    public boolean isChecked() {
        return this.mAdaptiveChargingManager.isEnabled();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mChecked = isChecked();
    }

    public boolean setChecked(boolean z) {
        this.mAdaptiveChargingManager.setEnabled(z);
        if (!z) {
            this.mAdaptiveChargingManager.setAdaptiveChargingDeadline(-1);
        }
        if (this.mChecked == z) {
            return true;
        }
        this.mChecked = z;
        FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, 1781, z);
        return true;
    }
}
