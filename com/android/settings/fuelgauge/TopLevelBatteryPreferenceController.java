package com.android.settings.fuelgauge;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.utils.ThreadUtils;

public class TopLevelBatteryPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, BatteryPreferenceController {
    private final BatteryBroadcastReceiver mBatteryBroadcastReceiver;
    private BatteryInfo mBatteryInfo;
    private BatteryStatusFeatureProvider mBatteryStatusFeatureProvider;
    private String mBatteryStatusLabel;
    protected boolean mIsBatteryPresent = true;
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

    public TopLevelBatteryPreferenceController(Context context, String str) {
        super(context, str);
        BatteryBroadcastReceiver batteryBroadcastReceiver = new BatteryBroadcastReceiver(this.mContext);
        this.mBatteryBroadcastReceiver = batteryBroadcastReceiver;
        batteryBroadcastReceiver.setBatteryChangedListener(new TopLevelBatteryPreferenceController$$ExternalSyntheticLambda0(this));
        this.mBatteryStatusFeatureProvider = FeatureFactory.getFactory(context).getBatteryStatusFeatureProvider(context);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i) {
        if (i == 5) {
            this.mIsBatteryPresent = false;
        }
        BatteryInfo.getBatteryInfo(this.mContext, (BatteryInfo.Callback) new TopLevelBatteryPreferenceController$$ExternalSyntheticLambda1(this), true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BatteryInfo batteryInfo) {
        this.mBatteryInfo = batteryInfo;
        updateState(this.mPreference);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(C0444R.bool.config_show_top_level_battery) ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        this.mBatteryBroadcastReceiver.register();
    }

    public void onStop() {
        this.mBatteryBroadcastReceiver.unRegister();
    }

    public CharSequence getSummary() {
        return getSummary(true);
    }

    private CharSequence getSummary(boolean z) {
        if (!this.mIsBatteryPresent) {
            return this.mContext.getText(C0444R.string.battery_missing_message);
        }
        return getDashboardLabel(this.mContext, this.mBatteryInfo, z);
    }

    /* access modifiers changed from: protected */
    public CharSequence getDashboardLabel(Context context, BatteryInfo batteryInfo, boolean z) {
        if (batteryInfo == null || context == null) {
            return null;
        }
        if (z) {
            setSummaryAsync(batteryInfo);
        }
        String str = this.mBatteryStatusLabel;
        return str == null ? generateLabel(batteryInfo) : str;
    }

    private void setSummaryAsync(BatteryInfo batteryInfo) {
        ThreadUtils.postOnBackgroundThread((Runnable) new TopLevelBatteryPreferenceController$$ExternalSyntheticLambda2(this, batteryInfo));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSummaryAsync$3(BatteryInfo batteryInfo) {
        ThreadUtils.postOnMainThread(new TopLevelBatteryPreferenceController$$ExternalSyntheticLambda3(this, this.mBatteryStatusFeatureProvider.triggerBatteryStatusUpdate(this, batteryInfo), batteryInfo));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSummaryAsync$2(boolean z, BatteryInfo batteryInfo) {
        if (!z) {
            this.mBatteryStatusLabel = null;
        }
        Preference preference = this.mPreference;
        CharSequence charSequence = this.mBatteryStatusLabel;
        if (charSequence == null) {
            charSequence = generateLabel(batteryInfo);
        }
        preference.setSummary(charSequence);
    }

    private CharSequence generateLabel(BatteryInfo batteryInfo) {
        CharSequence charSequence;
        if (!batteryInfo.discharging && (charSequence = batteryInfo.chargeLabel) != null) {
            return charSequence;
        }
        CharSequence charSequence2 = batteryInfo.remainingLabel;
        if (charSequence2 == null) {
            return batteryInfo.batteryPercentString;
        }
        return this.mContext.getString(C0444R.string.power_remaining_settings_home_page, new Object[]{batteryInfo.batteryPercentString, charSequence2});
    }

    public void updateBatteryStatus(String str, BatteryInfo batteryInfo) {
        CharSequence summary;
        this.mBatteryStatusLabel = str;
        if (this.mPreference != null && (summary = getSummary(false)) != null) {
            this.mPreference.setSummary(summary);
        }
    }

    protected static ComponentName convertClassPathToComponentName(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String[] split = str.split("\\.");
        int length = split.length - 1;
        if (length < 0) {
            return null;
        }
        int length2 = (str.length() - split[length].length()) - 1;
        return new ComponentName(length2 > 0 ? str.substring(0, length2) : "", split[length]);
    }
}
