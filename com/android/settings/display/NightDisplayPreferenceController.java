package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.PrimarySwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class NightDisplayPreferenceController extends TogglePreferenceController implements NightDisplayListener.Callback, LifecycleObserver, OnStart, OnStop {
    private final ColorDisplayManager mColorDisplayManager;
    private final NightDisplayListener mNightDisplayListener;
    private PrimarySwitchPreference mPreference;
    private final NightDisplayTimeFormatter mTimeFormatter;

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
        return C0444R.string.menu_key_display;
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

    public NightDisplayPreferenceController(Context context, String str) {
        super(context, str);
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        this.mNightDisplayListener = new NightDisplayListener(context);
        this.mTimeFormatter = new NightDisplayTimeFormatter(context);
    }

    public static boolean isSuggestionComplete(Context context) {
        if (context.getResources().getBoolean(C0444R.bool.config_night_light_suggestion_enabled) && ((ColorDisplayManager) context.getSystemService(ColorDisplayManager.class)).getNightDisplayAutoMode() == 0) {
            return false;
        }
        return true;
    }

    public void onStart() {
        this.mNightDisplayListener.setCallback(this);
    }

    public void onStop() {
        this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (PrimarySwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        return ColorDisplayManager.isNightDisplayAvailable(this.mContext) ? 0 : 3;
    }

    public boolean isChecked() {
        return this.mColorDisplayManager.isNightDisplayActivated();
    }

    public boolean setChecked(boolean z) {
        return this.mColorDisplayManager.setNightDisplayActivated(z);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setSummary((CharSequence) this.mTimeFormatter.getAutoModeSummary(this.mContext, this.mColorDisplayManager));
    }

    public void onActivated(boolean z) {
        updateState(this.mPreference);
    }
}
