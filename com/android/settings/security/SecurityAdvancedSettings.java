package com.android.settings.security;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.window.C0444R;
import com.android.settings.biometrics.combination.CombinedBiometricProfileStatusPreferenceController;
import com.android.settings.biometrics.face.FaceProfileStatusPreferenceController;
import com.android.settings.biometrics.fingerprint.FingerprintProfileStatusPreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.safetycenter.SafetyCenterStatusHolder;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.security.trustagent.TrustAgentListPreferenceController;
import com.android.settings.widget.PreferenceCategoryController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class SecurityAdvancedSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.security_advanced_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return SecurityAdvancedSettings.buildPreferenceControllers(context, (Lifecycle) null, (DashboardFragment) null);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SecurityAdvancedSettings";
    }

    public int getMetricsCategory() {
        return 1885;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.security_advanced_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterpriseStringTitle("unlock_set_or_change_profile", "Settings.WORK_PROFILE_SET_UNLOCK_LAUNCH_PICKER_TITLE", C0444R.string.unlock_set_unlock_launch_picker_title_profile);
        replaceEnterpriseStringSummary("unification", "Settings.WORK_PROFILE_UNIFY_LOCKS_SUMMARY", C0444R.string.lock_settings_profile_unification_summary);
        replaceEnterpriseStringTitle(FingerprintProfileStatusPreferenceController.KEY_FINGERPRINT_SETTINGS, "Settings.FINGERPRINT_FOR_WORK", C0444R.string.security_settings_work_fingerprint_preference_title);
        replaceEnterpriseStringTitle("manage_device_admin", "Settings.MANAGE_DEVICE_ADMIN_APPS", C0444R.string.manage_device_admin);
        replaceEnterpriseStringTitle("security_category_profile", "Settings.WORK_PROFILE_SECURITY_TITLE", C0444R.string.lock_settings_profile_title);
        replaceEnterpriseStringTitle("enterprise_privacy", "Settings.MANAGED_DEVICE_INFO", C0444R.string.enterprise_privacy_settings);
    }

    public String getCategoryKey() {
        Context context = getContext();
        if (context == null) {
            return "com.android.settings.category.ia.legacy_advanced_security";
        }
        if (SafetyCenterStatusHolder.get().isEnabled(context)) {
            return "com.android.settings.category.ia.advanced_security";
        }
        SecuritySettingsFeatureProvider securitySettingsFeatureProvider = FeatureFactory.getFactory(context).getSecuritySettingsFeatureProvider();
        if (securitySettingsFeatureProvider.hasAlternativeSecuritySettingsFragment()) {
            return securitySettingsFeatureProvider.getAlternativeAdvancedSettingsCategoryKey();
        }
        return "com.android.settings.category.ia.legacy_advanced_security";
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (!((TrustAgentListPreferenceController) use(TrustAgentListPreferenceController.class)).handleActivityResult(i, i2) && !((LockUnificationPreferenceController) use(LockUnificationPreferenceController.class)).handleActivityResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle, DashboardFragment dashboardFragment) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new TrustAgentListPreferenceController(context, dashboardFragment, lifecycle));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new ChangeProfileScreenLockPreferenceController(context, dashboardFragment));
        arrayList2.add(new LockUnificationPreferenceController(context, dashboardFragment));
        arrayList2.add(new VisiblePatternProfilePreferenceController(context, lifecycle));
        arrayList2.add(new FaceProfileStatusPreferenceController(context, (androidx.lifecycle.Lifecycle) lifecycle));
        arrayList2.add(new FingerprintProfileStatusPreferenceController(context, (androidx.lifecycle.Lifecycle) lifecycle));
        arrayList2.add(new CombinedBiometricProfileStatusPreferenceController(context, (androidx.lifecycle.Lifecycle) lifecycle));
        arrayList.add(new PreferenceCategoryController(context, "security_category_profile").setChildren(arrayList2));
        arrayList.addAll(arrayList2);
        return arrayList;
    }
}
