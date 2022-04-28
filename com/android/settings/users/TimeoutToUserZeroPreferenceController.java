package com.android.settings.users;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import java.util.Arrays;
import java.util.List;

public class TimeoutToUserZeroPreferenceController extends BasePreferenceController {
    private final String[] mEntries = this.mContext.getResources().getStringArray(C0444R.array.switch_to_user_zero_when_docked_timeout_entries);
    private final String[] mValues = this.mContext.getResources().getStringArray(C0444R.array.switch_to_user_zero_when_docked_timeout_values);

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

    public TimeoutToUserZeroPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        updateState(preferenceScreen.findPreference(getPreferenceKey()));
    }

    public int getAvailabilityStatus() {
        if (!this.mContext.getResources().getBoolean(17891644)) {
            return 3;
        }
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "user_switcher_enabled", 0) != 1) {
            return 2;
        }
        if (UserHandle.myUserId() == 0) {
            return 4;
        }
        return 0;
    }

    public CharSequence getSummary() {
        String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "timeout_to_user_zero", UserHandle.myUserId());
        List asList = Arrays.asList(this.mValues);
        if (stringForUser == null) {
            stringForUser = this.mValues[0];
        }
        return this.mEntries[asList.indexOf(stringForUser)];
    }
}
