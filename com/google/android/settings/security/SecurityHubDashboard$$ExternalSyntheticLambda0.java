package com.google.android.settings.security;

import android.os.Bundle;
import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityHubDashboard$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ SecurityHubDashboard f$0;
    public final /* synthetic */ Bundle f$1;

    public /* synthetic */ SecurityHubDashboard$$ExternalSyntheticLambda0(SecurityHubDashboard securityHubDashboard, Bundle bundle) {
        this.f$0 = securityHubDashboard;
        this.f$1 = bundle;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$updateSecurityPreference$2(this.f$1, preference);
    }
}
