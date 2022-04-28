package com.android.settings.applications;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.permission.PermissionControllerManager;
import android.provider.DeviceConfig;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import java.util.concurrent.Executor;

public final class HibernatedAppsPreferenceController extends BasePreferenceController implements LifecycleObserver {
    private static final String TAG = "HibernatedAppsPrefController";
    private boolean mLoadedUnusedCount;
    private boolean mLoadingUnusedApps;
    private final Executor mMainExecutor;
    private PreferenceScreen mScreen;
    private int mUnusedCount;

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

    public HibernatedAppsPreferenceController(Context context, String str) {
        this(context, str, context.getMainExecutor());
    }

    HibernatedAppsPreferenceController(Context context, String str, Executor executor) {
        super(context, str);
        this.mUnusedCount = 0;
        this.mMainExecutor = executor;
    }

    public int getAvailabilityStatus() {
        return isHibernationEnabled() ? 0 : 2;
    }

    public CharSequence getSummary() {
        if (!this.mLoadedUnusedCount) {
            return this.mContext.getResources().getString(C0444R.string.summary_placeholder);
        }
        Resources resources = this.mContext.getResources();
        int i = this.mUnusedCount;
        return resources.getQuantityString(C0444R.plurals.unused_apps_summary, i, new Object[]{Integer.valueOf(i)});
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        updatePreference();
    }

    private void updatePreference() {
        if (this.mScreen != null && !this.mLoadingUnusedApps) {
            ((PermissionControllerManager) this.mContext.getSystemService(PermissionControllerManager.class)).getUnusedAppCount(this.mMainExecutor, new HibernatedAppsPreferenceController$$ExternalSyntheticLambda0(this));
            this.mLoadingUnusedApps = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePreference$0(int i) {
        this.mUnusedCount = i;
        this.mLoadingUnusedApps = false;
        this.mLoadedUnusedCount = true;
        refreshSummary(this.mScreen.findPreference(this.mPreferenceKey));
    }

    private static boolean isHibernationEnabled() {
        return DeviceConfig.getBoolean("app_hibernation", "app_hibernation_enabled", true);
    }
}
