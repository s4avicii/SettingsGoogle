package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;

public class BatteryPercentagePreferenceController extends BasePreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener {
    private Preference mPreference;

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

    public BatteryPercentagePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (!Utils.isBatteryPresent(this.mContext)) {
            onPreferenceChange(this.mPreference, Boolean.FALSE);
        }
    }

    public int getAvailabilityStatus() {
        if (!Utils.isBatteryPresent(this.mContext)) {
            return 2;
        }
        return this.mContext.getResources().getBoolean(17891383) ? 0 : 3;
    }

    public void updateState(Preference preference) {
        boolean z = false;
        SwitchPreference switchPreference = (SwitchPreference) preference;
        if (Settings.System.getInt(this.mContext.getContentResolver(), "status_bar_show_battery_percent", 0) == 1) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        Settings.System.putInt(this.mContext.getContentResolver(), "status_bar_show_battery_percent", booleanValue ? 1 : 0);
        FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, 1888, booleanValue);
        return true;
    }
}
