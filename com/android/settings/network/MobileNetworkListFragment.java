package com.android.settings.network;

import android.content.Context;
import android.os.UserManager;
import android.provider.SearchIndexableResource;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class MobileNetworkListFragment extends DashboardFragment {
    static final String KEY_PREFERENCE_CATEGORY_DOWNLOADED_SIM = "provider_model_downloaded_sim_category";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            ArrayList arrayList = new ArrayList();
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = C0444R.xml.network_provider_sims_list;
            arrayList.add(searchIndexableResource);
            return arrayList;
        }

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return ((UserManager) context.getSystemService(UserManager.class)).isAdminUser();
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "NetworkListFragment";
    }

    public int getMetricsCategory() {
        return 1627;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.network_provider_sims_list;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new NetworkProviderSimsCategoryController(context, "provider_model_sim_category", getSettingsLifecycle()));
        arrayList.add(new NetworkProviderDownloadedSimsCategoryController(context, KEY_PREFERENCE_CATEGORY_DOWNLOADED_SIM, getSettingsLifecycle()));
        return arrayList;
    }
}
