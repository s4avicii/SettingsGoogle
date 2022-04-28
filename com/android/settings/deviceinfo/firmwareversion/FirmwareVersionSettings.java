package com.android.settings.deviceinfo.firmwareversion;

import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class FirmwareVersionSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.firmware_version);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "FirmwareVersionSettings";
    }

    public int getMetricsCategory() {
        return 1247;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.firmware_version;
    }
}
