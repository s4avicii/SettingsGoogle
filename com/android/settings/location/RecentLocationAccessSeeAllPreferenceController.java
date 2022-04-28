package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.applications.RecentAppOpsAccess;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.AppPreference;
import java.util.ArrayList;

public class RecentLocationAccessSeeAllPreferenceController extends LocationBasePreferenceController {
    private PreferenceScreen mCategoryAllRecentLocationAccess;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private Preference mPreference;
    private final RecentAppOpsAccess mRecentLocationAccesses;
    private boolean mShowSystem = false;

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

    public RecentLocationAccessSeeAllPreferenceController(Context context, String str) {
        super(context, str);
        boolean z = false;
        this.mShowSystem = Settings.Secure.getInt(this.mContext.getContentResolver(), "locationShowSystemOps", 0) == 1 ? true : z;
        this.mRecentLocationAccesses = RecentAppOpsAccess.createForLocation(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public void onLocationModeChanged(int i, boolean z) {
        this.mCategoryAllRecentLocationAccess.setEnabled(this.mLocationEnabler.isEnabled(i));
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mCategoryAllRecentLocationAccess = (PreferenceScreen) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void updateState(Preference preference) {
        this.mCategoryAllRecentLocationAccess.removeAll();
        this.mPreference = preference;
        UserManager userManager = UserManager.get(this.mContext);
        ArrayList<RecentAppOpsAccess.Access> arrayList = new ArrayList<>();
        for (RecentAppOpsAccess.Access next : this.mRecentLocationAccesses.getAppListSorted(this.mShowSystem)) {
            if (RecentLocationAccessPreferenceController.isRequestMatchesProfileType(userManager, next, 3)) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            AppPreference appPreference = new AppPreference(this.mContext);
            appPreference.setTitle((int) C0444R.string.location_no_recent_apps);
            appPreference.setSelectable(false);
            this.mCategoryAllRecentLocationAccess.addPreference(appPreference);
            return;
        }
        for (RecentAppOpsAccess.Access createAppPreference : arrayList) {
            this.mCategoryAllRecentLocationAccess.addPreference(RecentLocationAccessPreferenceController.createAppPreference(preference.getContext(), createAppPreference, this.mFragment));
        }
    }

    public void setShowSystem(boolean z) {
        this.mShowSystem = z;
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
            this.mMetricsFeatureProvider.logClickedPreference(this.mPreference, getMetricsCategory());
        }
    }
}
