package com.google.android.settings.gestures.columbus;

import android.content.Context;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class ColumbusSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.columbus_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return ColumbusPreferenceController.isColumbusSupported(context);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ColumbusSettings";
    }

    public int getMetricsCategory() {
        return 1848;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.columbus_settings;
    }
}
