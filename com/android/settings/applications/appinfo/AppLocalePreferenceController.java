package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.util.FeatureFlagUtils;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.AppLocaleUtil;

public class AppLocalePreferenceController extends AppInfoPreferenceControllerBase {
    private static final String TAG = "AppLocalePreferenceController";

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

    public AppLocalePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!FeatureFlagUtils.isEnabled(this.mContext, "settings_app_language_selection") || !canDisplayLocaleUi()) ? 2 : 0;
    }

    /* access modifiers changed from: protected */
    public Class<? extends SettingsPreferenceFragment> getDetailFragmentClass() {
        return AppLocaleDetails.class;
    }

    public CharSequence getSummary() {
        return AppLocaleDetails.getSummary(this.mContext, this.mParent.getAppEntry().info.packageName);
    }

    /* access modifiers changed from: package-private */
    public boolean canDisplayLocaleUi() {
        return AppLocaleUtil.canDisplayLocaleUi(this.mContext, this.mParent.getAppEntry());
    }
}
