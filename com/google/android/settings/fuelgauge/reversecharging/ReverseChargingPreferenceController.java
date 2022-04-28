package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settingslib.Utils;

public class ReverseChargingPreferenceController extends ReverseChargingBasePreferenceController {
    Preference mPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
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

    public ReverseChargingPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState() {
        updateState(this.mPreference);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        int thresholdLevel = getThresholdLevel();
        boolean isReverseChargingOn = this.mReverseChargingManager.isReverseChargingOn();
        if (isOverheat()) {
            preference.setSummary((CharSequence) this.mContext.getString(C0444R.string.reverse_charging_overheat_summary));
        } else if (isPowerSaveMode()) {
            preference.setSummary((CharSequence) this.mContext.getString(C0444R.string.reverse_charging_power_save_mode_is_on_message));
        } else if (this.mReverseChargingManager.isOnWirelessCharge()) {
            preference.setSummary((CharSequence) this.mContext.getString(C0444R.string.reverse_charging_charging_wirelessly_message));
        } else if (this.mIsUsbPlugIn) {
            preference.setSummary((CharSequence) this.mContext.getString(C0444R.string.reverse_charging_unplug_usb_cable_message));
        } else {
            int i = this.mLevel;
            if (i < 10) {
                preference.setSummary((CharSequence) this.mContext.getString(C0444R.string.reverse_charging_warning_summary, new Object[]{Utils.formatPercentage(10)}));
            } else if (thresholdLevel >= i) {
                preference.setSummary((CharSequence) this.mContext.getString(C0444R.string.reverse_charging_sharing_level_message));
            } else {
                preference.setSummary((CharSequence) this.mContext.getString(isReverseChargingOn ? C0444R.string.reverse_charging_is_on_summary : C0444R.string.reverse_charging_is_off_summary));
            }
        }
    }
}
