package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.widget.PreferenceCategoryController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;

public class NetworkProviderSimsCategoryController extends PreferenceCategoryController implements LifecycleObserver {
    private static final String KEY_PREFERENCE_CATEGORY_SIM = "provider_model_sim_category";
    private static final String LOG_TAG = "NetworkProviderSimsCategoryController";
    private NetworkProviderSimListController mNetworkProviderSimListController;
    private PreferenceCategory mPreferenceCategory;

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

    public NetworkProviderSimsCategoryController(Context context, String str, Lifecycle lifecycle) {
        super(context, str);
        this.mNetworkProviderSimListController = new NetworkProviderSimListController(this.mContext, lifecycle);
    }

    public int getAvailabilityStatus() {
        NetworkProviderSimListController networkProviderSimListController = this.mNetworkProviderSimListController;
        return (networkProviderSimListController == null || !networkProviderSimListController.isAvailable()) ? 2 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mNetworkProviderSimListController.displayPreference(preferenceScreen);
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(KEY_PREFERENCE_CATEGORY_SIM);
        this.mPreferenceCategory = preferenceCategory;
        if (preferenceCategory == null) {
            Log.d(LOG_TAG, "displayPreference(), Can not find the category.");
        } else {
            preferenceCategory.setVisible(isAvailable());
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        PreferenceCategory preferenceCategory = this.mPreferenceCategory;
        if (preferenceCategory == null) {
            Log.d(LOG_TAG, "updateState(), Can not find the category.");
            return;
        }
        this.mPreferenceCategory.setTitle((CharSequence) this.mContext.getString(preferenceCategory.getPreferenceCount() > 1 ? C0444R.string.provider_network_settings_title : C0444R.string.sim_category_title));
    }
}
