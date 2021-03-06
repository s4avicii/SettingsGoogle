package com.android.settings.location;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.widget.FooterPreference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationSettingsFooterPreferenceController extends LocationBasePreferenceController {
    private static final Intent INJECT_INTENT = new Intent("com.android.settings.location.DISPLAYED_FOOTER");
    private static final String PARAGRAPH_SEPARATOR = "<br><br>";
    private static final String TAG = "LocationFooter";
    private FooterPreference mFooterPreference;
    private String mInjectedFooterString;
    private boolean mLocationEnabled;
    private final PackageManager mPackageManager;

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

    public LocationSettingsFooterPreferenceController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mFooterPreference = (FooterPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onLocationModeChanged(int i, boolean z) {
        this.mLocationEnabled = this.mLocationEnabler.isEnabled(i);
        updateFooterPreference();
    }

    public void updateState(Preference preference) {
        for (FooterData next : getFooterData()) {
            try {
                this.mInjectedFooterString = this.mPackageManager.getResourcesForApplication(next.applicationInfo).getString(next.footerStringRes);
                updateFooterPreference();
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w(TAG, "Resources not found for application " + next.applicationInfo.packageName);
            }
        }
    }

    private void updateFooterPreference() {
        String string = this.mContext.getString(C0444R.string.location_settings_footer_general);
        if (!this.mLocationEnabled) {
            string = this.mContext.getString(C0444R.string.location_settings_footer_location_off) + PARAGRAPH_SEPARATOR + string;
        } else if (!TextUtils.isEmpty(this.mInjectedFooterString)) {
            string = Html.escapeHtml(this.mInjectedFooterString) + PARAGRAPH_SEPARATOR + string;
        }
        FooterPreference footerPreference = this.mFooterPreference;
        if (footerPreference != null) {
            footerPreference.setTitle((CharSequence) Html.fromHtml(string));
            this.mFooterPreference.setLearnMoreAction(new C1003x1c41a299(this));
            this.mFooterPreference.setLearnMoreContentDescription(this.mContext.getString(C0444R.string.location_settings_footer_learn_more_content_description));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFooterPreference$0(View view) {
        openLocationLearnMoreLink();
    }

    private void openLocationLearnMoreLink() {
        DashboardFragment dashboardFragment = this.mFragment;
        Context context = this.mContext;
        dashboardFragment.startActivityForResult(HelpUtils.getHelpIntent(context, context.getString(C0444R.string.location_settings_footer_learn_more_link), ""), 0);
    }

    private List<FooterData> getFooterData() {
        PackageManager packageManager = this.mPackageManager;
        Intent intent = INJECT_INTENT;
        List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 128);
        if (queryBroadcastReceivers == null) {
            Log.e(TAG, "Unable to resolve intent " + intent);
            return Collections.emptyList();
        }
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Found broadcast receivers: " + queryBroadcastReceivers);
        }
        ArrayList arrayList = new ArrayList(queryBroadcastReceivers.size());
        for (ResolveInfo next : queryBroadcastReceivers) {
            ActivityInfo activityInfo = next.activityInfo;
            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            if ((applicationInfo.flags & 1) == 0) {
                Log.w(TAG, "Ignoring attempt to inject footer from app not in system image: " + next);
            } else {
                Bundle bundle = activityInfo.metaData;
                if (bundle != null) {
                    int i = bundle.getInt("com.android.settings.location.FOOTER_STRING");
                    if (i == 0) {
                        Log.w(TAG, "No mapping of integer exists for com.android.settings.location.FOOTER_STRING");
                    } else {
                        arrayList.add(new FooterData(i, applicationInfo));
                    }
                } else if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "No METADATA in broadcast receiver " + activityInfo.name);
                }
            }
        }
        return arrayList;
    }

    private static class FooterData {
        public final ApplicationInfo applicationInfo;
        public final int footerStringRes;

        FooterData(int i, ApplicationInfo applicationInfo2) {
            this.footerStringRes = i;
            this.applicationInfo = applicationInfo2;
        }
    }
}
