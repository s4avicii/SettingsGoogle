package com.android.settings.fuelgauge.batterytip.tips;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.window.C0444R;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class EarlyWarningTip extends BatteryTip {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BatteryTip createFromParcel(Parcel parcel) {
            return new EarlyWarningTip(parcel);
        }

        public BatteryTip[] newArray(int i) {
            return new EarlyWarningTip[i];
        }
    };
    private boolean mPowerSaveModeOn;

    public EarlyWarningTip(int i, boolean z) {
        super(3, i, false);
        this.mPowerSaveModeOn = z;
    }

    public EarlyWarningTip(Parcel parcel) {
        super(parcel);
        this.mPowerSaveModeOn = parcel.readBoolean();
    }

    public CharSequence getTitle(Context context) {
        this.mState = C0444R.string.battery_tip_early_heads_up_title;
        return context.getString(C0444R.string.battery_tip_early_heads_up_title);
    }

    public CharSequence getSummary(Context context) {
        this.mState = C0444R.string.battery_tip_early_heads_up_summary;
        return context.getString(C0444R.string.battery_tip_early_heads_up_summary);
    }

    public int getIconId() {
        this.mState = C0444R.C0447drawable.ic_battery_status_bad_24dp;
        return C0444R.C0447drawable.ic_battery_status_bad_24dp;
    }

    public int getIconTintColorId() {
        this.mState = C0444R.C0446color.battery_bad_color_light;
        return C0444R.C0446color.battery_bad_color_light;
    }

    public void updateState(BatteryTip batteryTip) {
        EarlyWarningTip earlyWarningTip = (EarlyWarningTip) batteryTip;
        if (earlyWarningTip.mState == 0) {
            this.mState = 0;
        } else if (earlyWarningTip.mPowerSaveModeOn) {
            this.mState = 2;
        } else {
            this.mState = earlyWarningTip.getState();
        }
        this.mPowerSaveModeOn = earlyWarningTip.mPowerSaveModeOn;
    }

    public void log(Context context, MetricsFeatureProvider metricsFeatureProvider) {
        metricsFeatureProvider.action(context, 1351, this.mState);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeBoolean(this.mPowerSaveModeOn);
    }
}
