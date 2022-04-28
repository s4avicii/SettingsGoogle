package com.google.android.settings.aware;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.FeatureFlagUtils;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;

public class SkipGestureDirectionPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
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

    public SkipGestureDirectionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_skip_direction_mutable") ? 0 : 4;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ListPreference listPreference = (ListPreference) preference;
        listPreference.setValue(isDirectionRTL() ? "0" : "1");
        listPreference.setSummary(getSummary());
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "skip_gesture_direction", Integer.parseInt((String) obj));
        updateState(preference);
        return true;
    }

    public CharSequence getSummary() {
        if (isDirectionRTL()) {
            return this.mContext.getResources().getString(C0444R.string.gesture_skip_direction_rtl);
        }
        return this.mContext.getResources().getString(C0444R.string.gesture_skip_direction_ltr);
    }

    private boolean isDirectionRTL() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "skip_gesture_direction", 0, -2) == 0;
    }
}
