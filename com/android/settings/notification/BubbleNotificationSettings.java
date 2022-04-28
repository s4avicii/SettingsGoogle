package com.android.settings.notification;

import androidx.window.C0444R;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class BubbleNotificationSettings extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.bubble_notification_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BubbleNotiSettings";
    }

    public int getMetricsCategory() {
        return 1699;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.bubble_notification_settings;
    }
}
