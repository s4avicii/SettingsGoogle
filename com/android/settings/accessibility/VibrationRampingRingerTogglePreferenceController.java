package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settings.accessibility.VibrationPreferenceConfig;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class VibrationRampingRingerTogglePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final AudioManager mAudioManager;
    private final DeviceConfigProvider mDeviceConfigProvider;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final VibrationPreferenceConfig.SettingObserver mRingSettingObserver;
    private final VibrationPreferenceConfig mRingVibrationPreferenceConfig;
    private final ContentObserver mSettingObserver;

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

    protected static class DeviceConfigProvider {
        protected DeviceConfigProvider() {
        }

        public boolean isRampingRingerEnabledOnTelephonyConfig() {
            return DeviceConfig.getBoolean("telephony", "ramping_ringer_enabled", false);
        }
    }

    public VibrationRampingRingerTogglePreferenceController(Context context, String str) {
        this(context, str, new DeviceConfigProvider());
    }

    protected VibrationRampingRingerTogglePreferenceController(Context context, String str, DeviceConfigProvider deviceConfigProvider) {
        super(context, str);
        this.mDeviceConfigProvider = deviceConfigProvider;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        RingVibrationPreferenceConfig ringVibrationPreferenceConfig = new RingVibrationPreferenceConfig(context);
        this.mRingVibrationPreferenceConfig = ringVibrationPreferenceConfig;
        this.mRingSettingObserver = new VibrationPreferenceConfig.SettingObserver(ringVibrationPreferenceConfig);
        this.mSettingObserver = new ContentObserver(new Handler(true)) {
            public void onChange(boolean z, Uri uri) {
                VibrationRampingRingerTogglePreferenceController vibrationRampingRingerTogglePreferenceController = VibrationRampingRingerTogglePreferenceController.this;
                vibrationRampingRingerTogglePreferenceController.updateState(vibrationRampingRingerTogglePreferenceController.mPreference);
            }
        };
    }

    public int getAvailabilityStatus() {
        return (!Utils.isVoiceCapable(this.mContext) || this.mDeviceConfigProvider.isRampingRingerEnabledOnTelephonyConfig()) ? 3 : 0;
    }

    public void onStart() {
        this.mRingSettingObserver.register(this.mContext);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("apply_ramping_ringer"), false, this.mSettingObserver);
    }

    public void onStop() {
        this.mRingSettingObserver.unregister(this.mContext);
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingObserver);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = findPreference;
        this.mRingSettingObserver.onDisplayPreference(this, findPreference);
        this.mPreference.setEnabled(isRingVibrationEnabled());
    }

    public boolean isChecked() {
        return isRingVibrationEnabled() && this.mAudioManager.isRampingRingerEnabled();
    }

    public boolean setChecked(boolean z) {
        if (!isRingVibrationEnabled()) {
            return true;
        }
        this.mAudioManager.setRampingRingerEnabled(z);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            preference.setEnabled(isRingVibrationEnabled());
        }
    }

    private boolean isRingVibrationEnabled() {
        return this.mRingVibrationPreferenceConfig.isPreferenceEnabled() && this.mRingVibrationPreferenceConfig.readIntensity() != 0;
    }
}
