package com.android.settings.fuelgauge.batterysaver;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.widget.Switch;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.fuelgauge.BatterySaverReceiver;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class BatterySaverButtonPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener, LifecycleObserver, OnStart, OnStop, BatterySaverReceiver.BatterySaverListener {
    private static final long SWITCH_ANIMATION_DURATION = 350;
    private final BatterySaverReceiver mBatterySaverReceiver;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final PowerManager mPowerManager;
    private MainSwitchPreference mPreference;

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
        return C0444R.string.menu_key_battery;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BatterySaverButtonPreferenceController(Context context, String str) {
        super(context, str);
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        BatterySaverReceiver batterySaverReceiver = new BatterySaverReceiver(context);
        this.mBatterySaverReceiver = batterySaverReceiver;
        batterySaverReceiver.setBatterySaverListener(this);
    }

    public Uri getSliceUri() {
        return new Uri.Builder().scheme("content").authority("android.settings.slices").appendPath("action").appendPath("battery_saver").build();
    }

    public void onStart() {
        this.mBatterySaverReceiver.setListening(true);
    }

    public void onStop() {
        this.mBatterySaverReceiver.setListening(false);
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = mainSwitchPreference;
        mainSwitchPreference.setTitle(this.mContext.getString(C0444R.string.battery_saver_master_switch_title));
        this.mPreference.addOnSwitchChangeListener(this);
        this.mPreference.updateStatus(isChecked());
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        setChecked(z);
    }

    public boolean isChecked() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public boolean setChecked(boolean z) {
        this.mPreference.updateStatus(isChecked());
        return BatterySaverUtils.setPowerSaveMode(this.mContext, z, false);
    }

    public void onPowerSaveModeChanged() {
        this.mHandler.postDelayed(new BatterySaverButtonPreferenceController$$ExternalSyntheticLambda0(this), SWITCH_ANIMATION_DURATION);
    }

    /* access modifiers changed from: private */
    /* renamed from: onPowerSaveModeChangedInternal */
    public void lambda$onPowerSaveModeChanged$0() {
        boolean isChecked = isChecked();
        MainSwitchPreference mainSwitchPreference = this.mPreference;
        if (mainSwitchPreference != null && mainSwitchPreference.isChecked() != isChecked) {
            this.mPreference.setChecked(isChecked);
        }
    }

    public void onBatteryChanged(boolean z) {
        MainSwitchPreference mainSwitchPreference = this.mPreference;
        if (mainSwitchPreference != null) {
            mainSwitchPreference.setEnabled(!z);
        }
    }
}
