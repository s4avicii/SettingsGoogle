package com.android.settings.deviceinfo.legal;

import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;

public class ModuleLicensesDashboard extends DashboardFragment {
    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ModuleLicensesDashboard";
    }

    public int getMetricsCategory() {
        return 1746;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.module_licenses;
    }
}
