package com.android.settings.uwb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.provider.Settings;
import android.uwb.UwbManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.TogglePreferenceController;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UwbPreferenceController extends TogglePreferenceController implements UwbManager.AdapterStateCallback, LifecycleObserver {
    @VisibleForTesting
    static final String KEY_UWB_SETTINGS = "uwb_settings";
    @VisibleForTesting
    private final BroadcastReceiver mAirplaneModeChangedReceiver;
    @VisibleForTesting
    boolean mAirplaneModeOn;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public Preference mPreference;
    @VisibleForTesting
    UwbManager mUwbManager;

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
        return C0444R.string.menu_key_connected_devices;
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

    public UwbPreferenceController(Context context, String str) {
        super(context, str);
        this.mHandler = new Handler(context.getMainLooper());
        if (isUwbSupportedOnDevice()) {
            this.mUwbManager = (UwbManager) context.getSystemService(UwbManager.class);
        }
        this.mAirplaneModeOn = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        this.mAirplaneModeChangedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                UwbPreferenceController uwbPreferenceController = UwbPreferenceController.this;
                boolean z = false;
                if (Settings.Global.getInt(uwbPreferenceController.mContext.getContentResolver(), "airplane_mode_on", 0) == 1) {
                    z = true;
                }
                uwbPreferenceController.mAirplaneModeOn = z;
                UwbPreferenceController uwbPreferenceController2 = UwbPreferenceController.this;
                uwbPreferenceController2.updateState(uwbPreferenceController2.mPreference);
            }
        };
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isUwbSupportedOnDevice() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.uwb");
    }

    public int getAvailabilityStatus() {
        if (!isUwbSupportedOnDevice()) {
            return 3;
        }
        return this.mAirplaneModeOn ? 5 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean isChecked() {
        int adapterState = this.mUwbManager.getAdapterState();
        return adapterState == 2 || adapterState == 1;
    }

    public boolean setChecked(boolean z) {
        this.mAirplaneModeOn = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        if (isUwbSupportedOnDevice()) {
            if (this.mAirplaneModeOn) {
                this.mUwbManager.setUwbEnabled(false);
            } else {
                this.mUwbManager.setUwbEnabled(z);
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStateChanged$0() {
        updateState(this.mPreference);
    }

    public void onStateChanged(int i, int i2) {
        this.mHandler.post(new UwbPreferenceController$$ExternalSyntheticLambda0(this));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (isUwbSupportedOnDevice()) {
            this.mUwbManager.registerAdapterStateCallback(this.mExecutor, this);
        }
        BroadcastReceiver broadcastReceiver = this.mAirplaneModeChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
        }
        refreshSummary(this.mPreference);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (isUwbSupportedOnDevice()) {
            this.mUwbManager.unregisterAdapterStateCallback(this);
        }
        BroadcastReceiver broadcastReceiver = this.mAirplaneModeChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(!this.mAirplaneModeOn);
        refreshSummary(preference);
    }

    public CharSequence getSummary() {
        if (this.mAirplaneModeOn) {
            return this.mContext.getResources().getString(C0444R.string.uwb_settings_summary_airplane_mode);
        }
        return this.mContext.getResources().getString(C0444R.string.uwb_settings_summary);
    }
}
