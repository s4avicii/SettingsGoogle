package com.android.settings.location;

import android.content.Context;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class WifiScanningFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.location_services_wifi_scanning) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return WifiScanningFragment.buildPreferenceControllers(context);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "WifiScanningFragment";
    }

    public int getMetricsCategory() {
        return 1868;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.location_services_wifi_scanning;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WifiScanningPreferenceController(context));
        return arrayList;
    }
}
