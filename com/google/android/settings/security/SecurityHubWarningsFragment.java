package com.google.android.settings.security;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import androidx.annotation.Keep;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceCategory;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;
import com.google.android.settings.security.SecurityContentManager;
import java.util.ArrayList;
import java.util.List;

@Keep
public class SecurityHubWarningsFragment extends SettingsPreferenceFragment implements SecurityContentManager.UiDataSubscriber {
    public static final String SECURITY_WARNINGS_CATEGORY_KEY = "security_hub_warnings_category";
    private static final String TAG = "SecurityHubWarnings";
    private SecurityContentManager mSecurityContentManager;
    List<SecurityWarning> mSecurityWarnings = new ArrayList();

    public /* bridge */ /* synthetic */ int getHelpResource() {
        return super.getHelpResource();
    }

    public int getMetricsCategory() {
        return 1887;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.security_hub_warnings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSecurityContentManager = SecurityContentManager.getInstance(getContext()).subscribe(this);
    }

    public void onSecurityHubUiDataChange() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new SecurityHubWarningsFragment$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWarningList */
    public void lambda$onSecurityHubUiDataChange$0() {
        List<SecurityWarning> securityWarnings = this.mSecurityContentManager.getSecurityWarnings();
        if (!this.mSecurityWarnings.equals(securityWarnings)) {
            Context context = getContext();
            int size = securityWarnings.size();
            PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference(SECURITY_WARNINGS_CATEGORY_KEY);
            preferenceCategory.removeAll();
            preferenceCategory.setTitle((CharSequence) context.getResources().getQuantityString(C0444R.plurals.security_settings_hub_warnings_title, size, new Object[]{Integer.valueOf(size)}));
            securityWarnings.forEach(new SecurityHubWarningsFragment$$ExternalSyntheticLambda1(this, context, preferenceCategory));
            this.mSecurityWarnings = securityWarnings;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWarningList$1(Context context, PreferenceCategory preferenceCategory, SecurityWarning securityWarning) {
        SecurityWarningPreference securityWarningPreference = new SecurityWarningPreference(context, (AttributeSet) null);
        securityWarningPreference.setSecurityWarning(securityWarning, this);
        preferenceCategory.addPreference(securityWarningPreference);
    }
}
