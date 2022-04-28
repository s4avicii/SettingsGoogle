package com.android.settings.applications.appinfo;

import android.app.AppOpsManager;
import android.apphibernation.AppHibernationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.permission.PermissionControllerManager;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.Slog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

public final class HibernationSwitchPreferenceController extends AppInfoPreferenceControllerBase implements LifecycleObserver, AppOpsManager.OnOpChangedListener, Preference.OnPreferenceChangeListener {
    private static final String TAG = "HibernationSwitchPrefController";
    private final AppOpsManager mAppOpsManager;
    private int mHibernationEligibility = -1;
    private boolean mHibernationEligibilityLoaded;
    private boolean mIsPackageExemptByDefault;
    boolean mIsPackageSet;
    private String mPackageName;
    private int mPackageUid;
    private final PermissionControllerManager mPermissionControllerManager;

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

    public HibernationSwitchPreferenceController(Context context, String str) {
        super(context, str);
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mPermissionControllerManager = (PermissionControllerManager) context.getSystemService(PermissionControllerManager.class);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mIsPackageSet) {
            this.mAppOpsManager.startWatchingMode("android:auto_revoke_permissions_if_unused", this.mPackageName, this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mAppOpsManager.stopWatchingMode(this);
    }

    public int getAvailabilityStatus() {
        return (!isHibernationEnabled() || !this.mIsPackageSet) ? 2 : 0;
    }

    /* access modifiers changed from: package-private */
    public void setPackage(String str) {
        boolean z;
        this.mPackageName = str;
        PackageManager packageManager = this.mContext.getPackageManager();
        int i = packageManager.hasSystemFeature("android.hardware.type.automotive") ? 30 : 29;
        try {
            this.mPackageUid = packageManager.getPackageUid(str, 0);
            if (!hibernationTargetsPreSApps() && packageManager.getTargetSdkVersion(str) <= i) {
                z = true;
            } else {
                z = false;
            }
            this.mIsPackageExemptByDefault = z;
            this.mIsPackageSet = true;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.w(TAG, "Package [" + this.mPackageName + "] is not found!");
            this.mIsPackageSet = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0005, code lost:
        r2 = r2.mHibernationEligibility;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isAppEligibleForHibernation() {
        /*
            r2 = this;
            boolean r0 = r2.mHibernationEligibilityLoaded
            r1 = 1
            if (r0 == 0) goto L_0x000d
            int r2 = r2.mHibernationEligibility
            if (r2 == r1) goto L_0x000d
            r0 = -1
            if (r2 == r0) goto L_0x000d
            goto L_0x000e
        L_0x000d:
            r1 = 0
        L_0x000e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.applications.appinfo.HibernationSwitchPreferenceController.isAppEligibleForHibernation():boolean");
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ((SwitchPreference) preference).setChecked(isAppEligibleForHibernation() && !isPackageHibernationExemptByUser());
        preference.setEnabled(isAppEligibleForHibernation());
        if (!this.mHibernationEligibilityLoaded) {
            this.mPermissionControllerManager.getHibernationEligibility(this.mPackageName, this.mContext.getMainExecutor(), new HibernationSwitchPreferenceController$$ExternalSyntheticLambda0(this, preference));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$0(Preference preference, int i) {
        this.mHibernationEligibility = i;
        this.mHibernationEligibilityLoaded = true;
        updateState(preference);
    }

    /* access modifiers changed from: package-private */
    public boolean isPackageHibernationExemptByUser() {
        if (!this.mIsPackageSet) {
            return true;
        }
        int unsafeCheckOpNoThrow = this.mAppOpsManager.unsafeCheckOpNoThrow("android:auto_revoke_permissions_if_unused", this.mPackageUid, this.mPackageName);
        if (unsafeCheckOpNoThrow == 3) {
            return this.mIsPackageExemptByDefault;
        }
        if (unsafeCheckOpNoThrow != 0) {
            return true;
        }
        return false;
    }

    public void onOpChanged(String str, String str2) {
        if ("android:auto_revoke_permissions_if_unused".equals(str) && TextUtils.equals(this.mPackageName, str2)) {
            updateState(this.mPreference);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        try {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            this.mAppOpsManager.setUidMode("android:auto_revoke_permissions_if_unused", this.mPackageUid, booleanValue ? 0 : 1);
            if (!booleanValue) {
                AppHibernationManager appHibernationManager = (AppHibernationManager) this.mContext.getSystemService(AppHibernationManager.class);
                appHibernationManager.setHibernatingForUser(this.mPackageName, false);
                appHibernationManager.setHibernatingGlobally(this.mPackageName, false);
            }
            return true;
        } catch (RuntimeException unused) {
            return false;
        }
    }

    private static boolean isHibernationEnabled() {
        return DeviceConfig.getBoolean("app_hibernation", "app_hibernation_enabled", true);
    }

    private static boolean hibernationTargetsPreSApps() {
        return DeviceConfig.getBoolean("app_hibernation", "app_hibernation_targets_pre_s_apps", false);
    }
}
