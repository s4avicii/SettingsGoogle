package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import androidx.window.C0444R;
import com.android.settings.widget.SettingsMainSwitchPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class VibrationMainSwitchPreferenceController extends SettingsMainSwitchPreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final ContentObserver mSettingObserver = new ContentObserver(new Handler(true)) {
        public void onChange(boolean z, Uri uri) {
            VibrationMainSwitchPreferenceController vibrationMainSwitchPreferenceController = VibrationMainSwitchPreferenceController.this;
            vibrationMainSwitchPreferenceController.updateState(vibrationMainSwitchPreferenceController.mSwitchPreference);
        }
    };

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
        return C0444R.string.menu_key_accessibility;
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

    public VibrationMainSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void onStart() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("vibrate_on"), false, this.mSettingObserver);
    }

    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingObserver);
    }

    public boolean isChecked() {
        return VibrationPreferenceConfig.isMainVibrationSwitchEnabled(this.mContext.getContentResolver());
    }

    public boolean setChecked(boolean z) {
        return Settings.System.putInt(this.mContext.getContentResolver(), "vibrate_on", z ? 1 : 0);
    }
}
