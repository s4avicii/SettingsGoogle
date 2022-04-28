package com.android.settings.datausage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import androidx.window.C0444R;
import com.android.settings.applications.appinfo.AppInfoDashboardFragment;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.datausage.AppStateDataUsageBridge;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreferenceHelper;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AppSwitchPreference;

public class UnrestrictedDataAccessPreference extends AppSwitchPreference implements DataSaverBackend.Listener {
    private final ApplicationsState mApplicationsState;
    private Drawable mCacheIcon;
    private final DataSaverBackend mDataSaverBackend;
    private final AppStateDataUsageBridge.DataUsageState mDataUsageState;
    private final ApplicationsState.AppEntry mEntry;
    private final RestrictedPreferenceHelper mHelper;
    private final DashboardFragment mParentFragment;

    public void onDataSaverChanged(boolean z) {
    }

    public UnrestrictedDataAccessPreference(Context context, ApplicationsState.AppEntry appEntry, ApplicationsState applicationsState, DataSaverBackend dataSaverBackend, DashboardFragment dashboardFragment) {
        super(context);
        this.mHelper = new RestrictedPreferenceHelper(context, this, (AttributeSet) null);
        this.mEntry = appEntry;
        this.mDataUsageState = (AppStateDataUsageBridge.DataUsageState) appEntry.extraInfo;
        appEntry.ensureLabel(context);
        this.mApplicationsState = applicationsState;
        this.mDataSaverBackend = dataSaverBackend;
        this.mParentFragment = dashboardFragment;
        ApplicationInfo applicationInfo = appEntry.info;
        setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfMeteredDataRestricted(context, applicationInfo.packageName, UserHandle.getUserId(applicationInfo.uid)));
        updateState();
        setKey(generateKey(appEntry));
        Drawable iconFromCache = AppUtils.getIconFromCache(appEntry);
        this.mCacheIcon = iconFromCache;
        if (iconFromCache != null) {
            setIcon(iconFromCache);
        } else {
            setIcon((int) C0444R.C0447drawable.empty_icon);
        }
    }

    static String generateKey(ApplicationsState.AppEntry appEntry) {
        return appEntry.info.packageName + "|" + appEntry.info.uid;
    }

    public void onAttached() {
        super.onAttached();
        this.mDataSaverBackend.addListener(this);
    }

    public void onDetached() {
        this.mDataSaverBackend.remListener(this);
        super.onDetached();
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
        if (dataUsageState == null || !dataUsageState.isDataSaverDenylisted) {
            super.onClick();
        } else {
            AppInfoDashboardFragment.startAppInfoFragment(AppDataUsage.class, C0444R.string.data_usage_app_summary_title, (Bundle) null, this.mParentFragment, this.mEntry);
        }
    }

    public void performClick() {
        if (!this.mHelper.performClick()) {
            super.performClick();
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        if (this.mCacheIcon == null) {
            ThreadUtils.postOnBackgroundThread((Runnable) new UnrestrictedDataAccessPreference$$ExternalSyntheticLambda0(this));
        }
        boolean isDisabledByAdmin = isDisabledByAdmin();
        View findViewById = preferenceViewHolder.findViewById(16908312);
        int i = 0;
        if (isDisabledByAdmin) {
            findViewById.setVisibility(0);
        } else {
            AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
            if (dataUsageState != null && dataUsageState.isDataSaverDenylisted) {
                i = 4;
            }
            findViewById.setVisibility(i);
        }
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1() {
        ThreadUtils.postOnMainThread(new UnrestrictedDataAccessPreference$$ExternalSyntheticLambda1(this, AppUtils.getIcon(getContext(), this.mEntry)));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(Drawable drawable) {
        setIcon(drawable);
        this.mCacheIcon = drawable;
    }

    public void onAllowlistStatusChanged(int i, boolean z) {
        AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
        if (dataUsageState != null && this.mEntry.info.uid == i) {
            dataUsageState.isDataSaverAllowlisted = z;
            updateState();
        }
    }

    public void onDenylistStatusChanged(int i, boolean z) {
        AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
        if (dataUsageState != null && this.mEntry.info.uid == i) {
            dataUsageState.isDataSaverDenylisted = z;
            updateState();
        }
    }

    public AppStateDataUsageBridge.DataUsageState getDataUsageState() {
        return this.mDataUsageState;
    }

    public ApplicationsState.AppEntry getEntry() {
        return this.mEntry;
    }

    public boolean isDisabledByAdmin() {
        return this.mHelper.isDisabledByAdmin();
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mHelper.setDisabledByAdmin(enforcedAdmin);
    }

    public void updateState() {
        setTitle((CharSequence) this.mEntry.label);
        AppStateDataUsageBridge.DataUsageState dataUsageState = this.mDataUsageState;
        if (dataUsageState != null) {
            setChecked(dataUsageState.isDataSaverAllowlisted);
            if (isDisabledByAdmin()) {
                setSummary((int) C0444R.string.disabled_by_admin);
            } else if (this.mDataUsageState.isDataSaverDenylisted) {
                setSummary((int) C0444R.string.restrict_background_blocklisted);
            } else {
                setSummary((CharSequence) "");
            }
        }
        notifyChanged();
    }
}
