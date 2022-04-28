package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import androidx.lifecycle.Lifecycle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.wifitrackerlib.WifiPickerTracker;

public class CarrierWifiTogglePreferenceController extends TogglePreferenceController implements WifiPickerTracker.WifiPickerTrackerCallback {
    protected static final String CARRIER_WIFI_NETWORK_PREF_KEY = "carrier_wifi_network";
    protected static final String CARRIER_WIFI_TOGGLE_PREF_KEY = "carrier_wifi_toggle";
    private static final String TAG = "CarrierWifiTogglePreferenceController";
    protected Preference mCarrierNetworkPreference;
    protected final Context mContext;
    protected boolean mIsCarrierProvisionWifiEnabled;
    protected int mSubId;
    protected WifiPickerTrackerHelper mWifiPickerTrackerHelper;

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

    public void onNumSavedNetworksChanged() {
    }

    public void onNumSavedSubscriptionsChanged() {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public CarrierWifiTogglePreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
    }

    public void init(Lifecycle lifecycle, int i) {
        this.mSubId = i;
        WifiPickerTrackerHelper wifiPickerTrackerHelper = new WifiPickerTrackerHelper(lifecycle, this.mContext, this);
        this.mWifiPickerTrackerHelper = wifiPickerTrackerHelper;
        this.mIsCarrierProvisionWifiEnabled = wifiPickerTrackerHelper.isCarrierNetworkProvisionEnabled(this.mSubId);
    }

    public int getAvailabilityStatus() {
        return this.mIsCarrierProvisionWifiEnabled ? 0 : 2;
    }

    public boolean isChecked() {
        return this.mWifiPickerTrackerHelper.isCarrierNetworkEnabled(this.mSubId);
    }

    public boolean setChecked(boolean z) {
        WifiPickerTrackerHelper wifiPickerTrackerHelper = this.mWifiPickerTrackerHelper;
        if (wifiPickerTrackerHelper == null) {
            return false;
        }
        wifiPickerTrackerHelper.setCarrierNetworkEnabled(z);
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mCarrierNetworkPreference = preferenceScreen.findPreference(CARRIER_WIFI_NETWORK_PREF_KEY);
        updateCarrierNetworkPreference();
    }

    public void onWifiStateChanged() {
        updateCarrierNetworkPreference();
    }

    public void onWifiEntriesChanged() {
        updateCarrierNetworkPreference();
    }

    /* access modifiers changed from: protected */
    public void updateCarrierNetworkPreference() {
        if (this.mCarrierNetworkPreference != null) {
            if (getAvailabilityStatus() != 0 || !isCarrierNetworkActive()) {
                this.mCarrierNetworkPreference.setVisible(false);
                return;
            }
            this.mCarrierNetworkPreference.setVisible(true);
            this.mCarrierNetworkPreference.setSummary((CharSequence) getCarrierNetworkSsid());
        }
    }

    /* access modifiers changed from: protected */
    public boolean isCarrierNetworkActive() {
        WifiPickerTrackerHelper wifiPickerTrackerHelper = this.mWifiPickerTrackerHelper;
        if (wifiPickerTrackerHelper == null) {
            return false;
        }
        return wifiPickerTrackerHelper.isCarrierNetworkActive();
    }

    /* access modifiers changed from: protected */
    public String getCarrierNetworkSsid() {
        WifiPickerTrackerHelper wifiPickerTrackerHelper = this.mWifiPickerTrackerHelper;
        if (wifiPickerTrackerHelper == null) {
            return null;
        }
        return wifiPickerTrackerHelper.getCarrierNetworkSsid();
    }
}
