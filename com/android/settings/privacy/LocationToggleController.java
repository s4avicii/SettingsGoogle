package com.android.settings.privacy;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.location.LocationEnabler;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class LocationToggleController extends TogglePreferenceController implements LocationEnabler.LocationModeChangeListener {
    private boolean mIsLocationEnabled = true;
    private final LocationEnabler mLocationEnabler;
    private RestrictedSwitchPreference mPreference;

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
        return 0;
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

    public LocationToggleController(Context context, String str, Lifecycle lifecycle) {
        super(context, str);
        LocationEnabler locationEnabler = new LocationEnabler(context, this, lifecycle);
        this.mLocationEnabler = locationEnabler;
        locationEnabler.refreshLocationMode();
    }

    public void onLocationModeChanged(int i, boolean z) {
        if (this.mPreference != null) {
            this.mIsLocationEnabled = this.mLocationEnabler.isEnabled(i);
            int myUserId = UserHandle.myUserId();
            RestrictedLockUtils.EnforcedAdmin shareLocationEnforcedAdmin = this.mLocationEnabler.getShareLocationEnforcedAdmin(myUserId);
            if (this.mLocationEnabler.hasShareLocationRestriction(myUserId) || shareLocationEnforcedAdmin == null) {
                this.mPreference.setEnabled(!z);
            } else {
                this.mPreference.setDisabledByAdmin(shareLocationEnforcedAdmin);
            }
            updateState(this.mPreference);
        }
    }

    public boolean isChecked() {
        return this.mIsLocationEnabled;
    }

    public boolean setChecked(boolean z) {
        this.mLocationEnabler.setLocationEnabled(z);
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mLocationEnabler.refreshLocationMode();
    }
}
