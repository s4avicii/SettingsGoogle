package com.google.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import androidx.window.C0444R;

public class ConnectivityHelperDeviceToDevicePreferenceController extends ConnectivityHelperBasePreferenceController {
    private static final String LOG_TAG = "ch_d2d";

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getKeyName() {
        return "d2d_notifications";
    }

    public String getLogTag() {
        return LOG_TAG;
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

    public ConnectivityHelperDeviceToDevicePreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean getDefaultValue() {
        return this.mContext.getResources().getBoolean(C0444R.bool.config_connectivity_helper_d2d_default_value);
    }

    public boolean getDeviceSupport() {
        return this.mContext.getResources().getBoolean(C0444R.bool.config_show_connectivity_helper_d2d);
    }
}
