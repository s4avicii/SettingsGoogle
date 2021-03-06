package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import java.util.HashMap;
import java.util.Map;

public class SelectLongPressTimeoutPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private int mLongPressTimeoutDefault;
    private final Map<String, String> mLongPressTimeoutValueToTitleMap = new HashMap();

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

    public SelectLongPressTimeoutPreferenceController(Context context, String str) {
        super(context, str);
        initLongPressTimeoutValueToTitleMap();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!(preference instanceof ListPreference)) {
            return false;
        }
        Settings.Secure.putInt(this.mContext.getContentResolver(), "long_press_timeout", Integer.parseInt((String) obj));
        updateState((ListPreference) preference);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference instanceof ListPreference) {
            ((ListPreference) preference).setValue(getLongPressTimeoutValue());
        }
    }

    public CharSequence getSummary() {
        return this.mLongPressTimeoutValueToTitleMap.get(getLongPressTimeoutValue());
    }

    private String getLongPressTimeoutValue() {
        return String.valueOf(Settings.Secure.getInt(this.mContext.getContentResolver(), "long_press_timeout", this.mLongPressTimeoutDefault));
    }

    private void initLongPressTimeoutValueToTitleMap() {
        if (this.mLongPressTimeoutValueToTitleMap.size() == 0) {
            String[] stringArray = this.mContext.getResources().getStringArray(C0444R.array.long_press_timeout_selector_values);
            this.mLongPressTimeoutDefault = Integer.parseInt(stringArray[0]);
            String[] stringArray2 = this.mContext.getResources().getStringArray(C0444R.array.long_press_timeout_selector_titles);
            int length = stringArray.length;
            for (int i = 0; i < length; i++) {
                this.mLongPressTimeoutValueToTitleMap.put(stringArray[i], stringArray2[i]);
            }
        }
    }
}
