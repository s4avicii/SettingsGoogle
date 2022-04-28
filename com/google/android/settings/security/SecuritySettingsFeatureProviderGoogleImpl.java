package com.google.android.settings.security;

import android.content.Context;
import android.util.FeatureFlagUtils;
import androidx.window.C0444R;
import com.android.settings.security.SecuritySettingsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.util.concurrent.Callable;

public class SecuritySettingsFeatureProviderGoogleImpl implements SecuritySettingsFeatureProvider {
    private final Context mContext;

    public SecuritySettingsFeatureProviderGoogleImpl(Context context) {
        this.mContext = context;
        ThreadUtils.postOnBackgroundThread((Callable) new C1825x7106468c(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$new$0() throws Exception {
        return Boolean.valueOf(SecurityContentManager.getInstance(this.mContext).getSecurityHubIsEnabled());
    }

    public boolean hasAlternativeSecuritySettingsFragment() {
        return SecurityContentManager.getInstance(this.mContext).getSecurityHubIsEnabled() && FeatureFlagUtils.isEnabled(this.mContext, "settings_enable_security_hub");
    }

    public String getAlternativeSecuritySettingsFragmentClassname() {
        return SecurityHubDashboard.class.getName();
    }

    public String getAlternativeAdvancedSettingsCategoryKey() {
        return this.mContext.getString(C0444R.string.config_alternative_advanced_security_category_key);
    }
}
