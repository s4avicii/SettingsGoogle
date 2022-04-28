package com.android.settings.datausage;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.NetworkTemplate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.net.DataUsageController;
import java.util.HashSet;
import java.util.Set;

public class WifiDataUsageSummaryPreferenceController extends DataUsageSummaryPreferenceController {
    final Set<String> mAllNetworkKeys;

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

    public WifiDataUsageSummaryPreferenceController(Activity activity, Lifecycle lifecycle, PreferenceFragmentCompat preferenceFragmentCompat, Set<String> set) {
        super(activity, lifecycle, preferenceFragmentCompat, -1);
        this.mAllNetworkKeys = new HashSet(set);
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            DataUsageSummaryPreference dataUsageSummaryPreference = (DataUsageSummaryPreference) preference;
            NetworkTemplate build = new NetworkTemplate.Builder(4).setWifiNetworkKeys(this.mAllNetworkKeys).build();
            if (this.mDataUsageController == null) {
                Context context = this.mContext;
                int i = this.mSubId;
                updateConfiguration(context, i, getSubscriptionInfo(i));
            }
            DataUsageController.DataUsageInfo dataUsageInfo = this.mDataUsageController.getDataUsageInfo(build);
            this.mDataInfoController.updateDataLimit(dataUsageInfo, this.mPolicyEditor.getPolicy(build));
            dataUsageSummaryPreference.setWifiMode(true, dataUsageInfo.period, true);
            dataUsageSummaryPreference.setChartEnabled(true);
            long j = dataUsageInfo.usageLevel;
            dataUsageSummaryPreference.setUsageNumbers(j, j, false);
            dataUsageSummaryPreference.setProgress(100.0f);
            dataUsageSummaryPreference.setLabels(DataUsageUtils.formatDataUsage(this.mContext, 0), DataUsageUtils.formatDataUsage(this.mContext, dataUsageInfo.usageLevel));
        }
    }
}
