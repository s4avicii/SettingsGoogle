package com.android.settings.deviceinfo.hardwareinfo;

import android.content.Context;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class HardwareInfoFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.hardware_info) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return context.getResources().getBoolean(C0444R.bool.config_show_device_model);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "HardwareInfo";
    }

    public int getMetricsCategory() {
        return 862;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.hardware_info;
    }
}
