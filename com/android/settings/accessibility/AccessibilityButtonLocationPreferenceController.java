package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import com.google.common.primitives.Ints;
import java.util.Optional;

public class AccessibilityButtonLocationPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private Optional<Integer> mDefaultLocation = Optional.empty();

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

    public AccessibilityButtonLocationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return AccessibilityUtil.isGestureNavigateEnabled(this.mContext) ? 2 : 0;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        ListPreference listPreference = (ListPreference) preference;
        Integer tryParse = Ints.tryParse((String) obj);
        if (tryParse == null) {
            return true;
        }
        Settings.Secure.putInt(this.mContext.getContentResolver(), "accessibility_button_mode", tryParse.intValue());
        updateState(listPreference);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ((ListPreference) preference).setValue(getCurrentAccessibilityButtonMode());
    }

    private String getCurrentAccessibilityButtonMode() {
        return String.valueOf(Settings.Secure.getInt(this.mContext.getContentResolver(), "accessibility_button_mode", getDefaultLocationValue()));
    }

    private int getDefaultLocationValue() {
        if (!this.mDefaultLocation.isPresent()) {
            this.mDefaultLocation = Optional.of(Integer.valueOf(Integer.parseInt(this.mContext.getResources().getStringArray(C0444R.array.accessibility_button_location_selector_values)[0])));
        }
        return this.mDefaultLocation.get().intValue();
    }
}
