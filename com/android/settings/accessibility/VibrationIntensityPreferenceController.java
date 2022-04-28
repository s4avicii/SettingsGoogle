package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.accessibility.VibrationPreferenceConfig;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.widget.SeekBarPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class VibrationIntensityPreferenceController extends SliderPreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final int mMaxIntensity;
    protected final VibrationPreferenceConfig mPreferenceConfig;
    private final VibrationPreferenceConfig.SettingObserver mSettingsContentObserver;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getMin() {
        return 0;
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

    protected VibrationIntensityPreferenceController(Context context, String str, VibrationPreferenceConfig vibrationPreferenceConfig) {
        this(context, str, vibrationPreferenceConfig, context.getResources().getInteger(C0444R.integer.config_vibration_supported_intensity_levels));
    }

    protected VibrationIntensityPreferenceController(Context context, String str, VibrationPreferenceConfig vibrationPreferenceConfig, int i) {
        super(context, str);
        this.mPreferenceConfig = vibrationPreferenceConfig;
        this.mSettingsContentObserver = new VibrationPreferenceConfig.SettingObserver(vibrationPreferenceConfig);
        this.mMaxIntensity = Math.min(3, i);
    }

    public void onStart() {
        this.mSettingsContentObserver.register(this.mContext);
    }

    public void onStop() {
        this.mSettingsContentObserver.unregister(this.mContext);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SeekBarPreference seekBarPreference = (SeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mSettingsContentObserver.onDisplayPreference(this, seekBarPreference);
        seekBarPreference.setEnabled(this.mPreferenceConfig.isPreferenceEnabled());
        seekBarPreference.setSummaryProvider(new VibrationIntensityPreferenceController$$ExternalSyntheticLambda0(this));
        seekBarPreference.setContinuousUpdates(true);
        seekBarPreference.setMin(getMin());
        seekBarPreference.setMax(getMax());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ CharSequence lambda$displayPreference$0(Preference preference) {
        return this.mPreferenceConfig.getSummary();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            preference.setEnabled(this.mPreferenceConfig.isPreferenceEnabled());
        }
    }

    public int getMax() {
        return this.mMaxIntensity;
    }

    public int getSliderPosition() {
        if (!this.mPreferenceConfig.isPreferenceEnabled()) {
            return getMin();
        }
        return Math.min(this.mPreferenceConfig.readIntensity(), getMax());
    }

    public boolean setSliderPosition(int i) {
        if (!this.mPreferenceConfig.isPreferenceEnabled()) {
            return false;
        }
        boolean updateIntensity = this.mPreferenceConfig.updateIntensity(calculateVibrationIntensity(i));
        if (updateIntensity && i != 0) {
            this.mPreferenceConfig.playVibrationPreview();
        }
        return updateIntensity;
    }

    private int calculateVibrationIntensity(int i) {
        int max = getMax();
        if (i < max) {
            return i;
        }
        if (max == 1) {
            return this.mPreferenceConfig.getDefaultIntensity();
        }
        return 3;
    }
}
