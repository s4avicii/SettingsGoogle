package com.android.settings.privacy;

import android.content.Context;
import android.os.Bundle;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.LockScreenNotificationPreferenceController;
import com.android.settings.safetycenter.SafetyCenterStatusHolder;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class PrivacyDashboardFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.privacy_dashboard_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return PrivacyDashboardFragment.buildPreferenceControllers(context, (Lifecycle) null);
        }

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return !SafetyCenterStatusHolder.get().isEnabled(context);
        }
    };

    public int getHelpResource() {
        return C0444R.string.help_url_privacy_dashboard;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "PrivacyDashboardFrag";
    }

    public int getMetricsCategory() {
        return 1587;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.privacy_dashboard_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterpriseStringTitle("privacy_lock_screen_work_profile_notifications", "Settings.WORK_PROFILE_LOCKED_NOTIFICATION_TITLE", C0444R.string.locked_work_profile_notification_title);
        replaceEnterpriseStringTitle("interact_across_profiles_privacy", "Settings.CONNECTED_WORK_AND_PERSONAL_APPS_TITLE", C0444R.string.interact_across_profiles_title);
        replaceEnterpriseStringTitle("privacy_work_profile_notifications_category", "Settings.WORK_PROFILE_NOTIFICATIONS_SECTION_HEADER", C0444R.string.profile_section_header);
        replaceEnterpriseStringTitle("work_policy_info", "Settings.WORK_PROFILE_PRIVACY_POLICY_INFO", C0444R.string.work_policy_privacy_settings);
        replaceEnterpriseStringSummary("work_policy_info", "Settings.WORK_PROFILE_PRIVACY_POLICY_INFO_SUMMARY", C0444R.string.work_policy_privacy_settings_summary);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        LockScreenNotificationPreferenceController lockScreenNotificationPreferenceController = new LockScreenNotificationPreferenceController(context, "privacy_lock_screen_notifications", "privacy_work_profile_notifications_category", "privacy_lock_screen_work_profile_notifications");
        if (lifecycle != null) {
            lifecycle.addObserver(lockScreenNotificationPreferenceController);
        }
        arrayList.add(lockScreenNotificationPreferenceController);
        return arrayList;
    }
}
