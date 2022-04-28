package com.android.settings.wifi.p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;

public class WifiP2pPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnPause, OnResume {
    private final IntentFilter mFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
    boolean mIsWifiDirectAllow;
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            WifiP2pPreferenceController.this.togglePreferences();
        }
    };
    private Preference mWifiDirectPref;
    private final WifiManager mWifiManager;

    public String getPreferenceKey() {
        return "wifi_direct";
    }

    public boolean isAvailable() {
        return true;
    }

    public WifiP2pPreferenceController(Context context, Lifecycle lifecycle, WifiManager wifiManager) {
        super(context);
        this.mWifiManager = wifiManager;
        this.mIsWifiDirectAllow = WifiEnterpriseRestrictionUtils.isWifiDirectAllowed(context);
        lifecycle.addObserver(this);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mWifiDirectPref = preferenceScreen.findPreference("wifi_direct");
        togglePreferences();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(isWifiP2pAvailable());
    }

    public void onResume() {
        this.mContext.registerReceiver(this.mReceiver, this.mFilter);
    }

    public void onPause() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    /* access modifiers changed from: private */
    public void togglePreferences() {
        Preference preference = this.mWifiDirectPref;
        if (preference != null) {
            preference.setEnabled(isWifiP2pAvailable());
        }
    }

    private boolean isWifiP2pAvailable() {
        return this.mWifiManager.isWifiEnabled() && this.mIsWifiDirectAllow;
    }
}
