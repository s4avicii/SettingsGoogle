package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.system.ResetDashboardFragment;

public class EraseEuiccDataController extends BasePreferenceController {
    private ResetDashboardFragment mHostFragment;

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

    public EraseEuiccDataController(Context context, String str) {
        super(context, str);
    }

    public void setFragment(ResetDashboardFragment resetDashboardFragment) {
        this.mHostFragment = resetDashboardFragment;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        EraseEuiccDataDialogFragment.show(this.mHostFragment);
        return true;
    }

    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.euicc") ? 1 : 3;
    }
}
