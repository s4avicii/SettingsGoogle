package com.android.settings.accessibility;

import android.os.Bundle;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class AccessibilityButtonFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.accessibility_button_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AccessibilityButtonFragment";
    }

    public int getMetricsCategory() {
        return 1870;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.accessibility_button_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivity().setTitle(AccessibilityUtil.isGestureNavigateEnabled(getPrefContext()) ? C0444R.string.accessibility_button_gesture_title : C0444R.string.accessibility_button_title);
    }
}
