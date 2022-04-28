package com.android.settings.display.darkmode;

import android.app.UiModeManager;
import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.PowerManager;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.display.TwilightLocationDialog;

public class DarkModeScheduleSelectorController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "DarkModeScheduleSelectorController";
    private final BedtimeSettings mBedtimeSettings;
    private int mCurrentMode;
    private final LocationManager mLocationManager;
    private final PowerManager mPowerManager;
    private DropDownPreference mPreference;
    private final UiModeManager mUiModeManager;

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

    public DarkModeScheduleSelectorController(Context context, String str) {
        super(context, str);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mLocationManager = (LocationManager) context.getSystemService(LocationManager.class);
        this.mBedtimeSettings = new BedtimeSettings(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (DropDownPreference) preferenceScreen.findPreference(getPreferenceKey());
        if (this.mBedtimeSettings.getBedtimeSettingsIntent() != null) {
            String[] stringArray = this.mContext.getResources().getStringArray(C0444R.array.dark_ui_scheduler_with_bedtime_preference_titles);
            this.mPreference.setEntries(stringArray);
            this.mPreference.setEntryValues(stringArray);
        }
    }

    public final void updateState(Preference preference) {
        this.mPreference.setEnabled(!this.mPowerManager.isPowerSaveMode());
        int currentMode = getCurrentMode();
        this.mCurrentMode = currentMode;
        this.mPreference.setValueIndex(currentMode);
    }

    private int getCurrentMode() {
        int i;
        int nightMode = this.mUiModeManager.getNightMode();
        if (nightMode == 0) {
            i = C0444R.string.dark_ui_auto_mode_auto;
        } else if (nightMode != 3) {
            i = C0444R.string.dark_ui_auto_mode_never;
        } else {
            boolean z = true;
            if (this.mBedtimeSettings.getBedtimeSettingsIntent() == null || this.mUiModeManager.getNightModeCustomType() != 1) {
                z = false;
            }
            i = z ? C0444R.string.dark_ui_auto_mode_custom_bedtime : C0444R.string.dark_ui_auto_mode_custom;
        }
        return this.mPreference.findIndexOfValue(this.mContext.getString(i));
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        int findIndexOfValue = this.mPreference.findIndexOfValue((String) obj);
        boolean z = false;
        if (findIndexOfValue == this.mCurrentMode) {
            return false;
        }
        if (findIndexOfValue == this.mPreference.findIndexOfValue(this.mContext.getString(C0444R.string.dark_ui_auto_mode_never))) {
            if ((this.mContext.getResources().getConfiguration().uiMode & 32) != 0) {
                z = true;
            }
            this.mUiModeManager.setNightMode(z ? 2 : 1);
        } else if (findIndexOfValue == this.mPreference.findIndexOfValue(this.mContext.getString(C0444R.string.dark_ui_auto_mode_auto))) {
            if (!this.mLocationManager.isLocationEnabled()) {
                TwilightLocationDialog.show(this.mContext);
                return true;
            }
            this.mUiModeManager.setNightMode(0);
        } else if (findIndexOfValue == this.mPreference.findIndexOfValue(this.mContext.getString(C0444R.string.dark_ui_auto_mode_custom))) {
            this.mUiModeManager.setNightMode(3);
        } else if (findIndexOfValue == this.mPreference.findIndexOfValue(this.mContext.getString(C0444R.string.dark_ui_auto_mode_custom_bedtime))) {
            this.mUiModeManager.setNightModeCustomType(1);
        }
        this.mCurrentMode = findIndexOfValue;
        return true;
    }
}
