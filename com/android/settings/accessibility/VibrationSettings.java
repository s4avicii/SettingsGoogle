package com.android.settings.accessibility;

import android.content.Context;
import android.os.Vibrator;
import android.provider.SearchIndexableResource;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import java.util.ArrayList;
import java.util.List;

public class VibrationSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return ((Vibrator) context.getSystemService(Vibrator.class)).hasVibrator();
        }

        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            ArrayList arrayList = new ArrayList();
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = VibrationSettings.getVibrationXmlResourceId(context);
            arrayList.add(searchIndexableResource);
            return arrayList;
        }
    };

    public int getHelpResource() {
        return C0444R.string.help_uri_accessibility_vibration;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "VibrationSettings";
    }

    public int getMetricsCategory() {
        return 1292;
    }

    /* access modifiers changed from: private */
    public static int getVibrationXmlResourceId(Context context) {
        return context.getResources().getInteger(C0444R.integer.config_vibration_supported_intensity_levels) > 1 ? C0444R.xml.accessibility_vibration_intensity_settings : C0444R.xml.accessibility_vibration_settings;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return getVibrationXmlResourceId(getContext());
    }
}
