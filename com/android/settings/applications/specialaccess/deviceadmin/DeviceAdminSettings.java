package com.android.settings.applications.specialaccess.deviceadmin;

import android.os.Bundle;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class DeviceAdminSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.device_admin_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DeviceAdminSettings";
    }

    public int getMetricsCategory() {
        return 516;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.device_admin_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterpriseStringTitle("device_admin_footer", "Settings.NO_DEVICE_ADMINS", C0444R.string.no_device_admins);
    }
}
