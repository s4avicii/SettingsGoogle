package com.android.settings.accessibility;

import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class ShortcutsSettingsFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.accessibility_shortcuts_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ShortcutsSettingsFragment";
    }

    public int getMetricsCategory() {
        return 1864;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.accessibility_shortcuts_settings;
    }
}
