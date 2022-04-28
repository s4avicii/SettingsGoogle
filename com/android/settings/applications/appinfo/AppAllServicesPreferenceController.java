package com.android.settings.applications.appinfo;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import java.util.Objects;

public class AppAllServicesPreferenceController extends AppInfoPreferenceControllerBase {
    private static final String SUMMARY_METADATA_KEY = "app_features_preference_summary";
    private static final String TAG = "AllServicesPrefControl";
    private boolean mCanPackageHandleAllServicesIntent = false;
    private boolean mIsLocationProvider = false;
    private final PackageManager mPackageManager;
    private String mPackageName;

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

    public AppAllServicesPreferenceController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        CharSequence storageSummary = getStorageSummary();
        if (storageSummary != null) {
            this.mPreference.setSummary(storageSummary);
        }
    }

    /* access modifiers changed from: package-private */
    public CharSequence getStorageSummary() {
        ResolveInfo resolveInfo = getResolveInfo(128);
        if (resolveInfo == null) {
            Log.d(TAG, "mResolveInfo is null.");
            return null;
        }
        Bundle bundle = resolveInfo.activityInfo.metaData;
        if (bundle != null) {
            try {
                return this.mPackageManager.getResourcesForActivity(new ComponentName(this.mPackageName, resolveInfo.activityInfo.name)).getString(bundle.getInt(SUMMARY_METADATA_KEY));
            } catch (Resources.NotFoundException unused) {
                Log.d(TAG, "Resource not found for summary string.");
            } catch (PackageManager.NameNotFoundException unused2) {
                Log.d(TAG, "Name of resource not found for summary string.");
            }
        }
        return null;
    }

    public int getAvailabilityStatus() {
        return (!this.mCanPackageHandleAllServicesIntent || !this.mIsLocationProvider) ? 2 : 0;
    }

    private boolean isLocationProvider() {
        LocationManager locationManager = (LocationManager) this.mContext.getSystemService(LocationManager.class);
        Objects.requireNonNull(locationManager);
        LocationManager locationManager2 = locationManager;
        return locationManager.isProviderPackage(this.mPackageName);
    }

    private boolean canPackageHandleIntent() {
        return getResolveInfo(0) != null;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!getPreferenceKey().equals(preference.getKey())) {
            return false;
        }
        startAllServicesActivity();
        return true;
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
        updateAvailabilityConditions();
    }

    private void updateAvailabilityConditions() {
        this.mCanPackageHandleAllServicesIntent = canPackageHandleIntent();
        this.mIsLocationProvider = isLocationProvider();
    }

    private void startAllServicesActivity() {
        Intent intent = new Intent("android.intent.action.VIEW_APP_FEATURES");
        intent.setComponent(new ComponentName(this.mPackageName, getResolveInfo(0).activityInfo.name));
        FragmentActivity activity = this.mParent.getActivity();
        if (activity != null) {
            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Log.e(TAG, "The app cannot handle android.intent.action.VIEW_APP_FEATURES");
            }
        }
    }

    private ResolveInfo getResolveInfo(int i) {
        if (this.mPackageName == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.VIEW_APP_FEATURES");
        intent.setPackage(this.mPackageName);
        return this.mPackageManager.resolveActivity(intent, i);
    }
}
