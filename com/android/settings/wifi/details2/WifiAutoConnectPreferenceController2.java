package com.android.settings.wifi.details2;

import android.content.Context;
import android.content.IntentFilter;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;
import com.android.wifitrackerlib.WifiEntry;

public class WifiAutoConnectPreferenceController2 extends TogglePreferenceController {
    private static final String KEY_AUTO_CONNECT = "auto_connect";
    private WifiEntry mWifiEntry;

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
        return C0444R.string.menu_key_network;
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

    public WifiAutoConnectPreferenceController2(Context context) {
        super(context, KEY_AUTO_CONNECT);
    }

    public void setWifiEntry(WifiEntry wifiEntry) {
        this.mWifiEntry = wifiEntry;
    }

    public int getAvailabilityStatus() {
        return this.mWifiEntry.canSetAutoJoinEnabled() ? 0 : 2;
    }

    public boolean isChecked() {
        return this.mWifiEntry.isAutoJoinEnabled();
    }

    public boolean setChecked(boolean z) {
        this.mWifiEntry.setAutoJoinEnabled(z);
        return true;
    }
}
