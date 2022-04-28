package com.google.android.settings.security;

import android.os.Bundle;
import android.view.View;
import com.android.settings.SettingsPreferenceFragment;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityWarningPreference$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ SecurityWarningPreference f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ SettingsPreferenceFragment f$2;

    public /* synthetic */ SecurityWarningPreference$$ExternalSyntheticLambda1(SecurityWarningPreference securityWarningPreference, Bundle bundle, SettingsPreferenceFragment settingsPreferenceFragment) {
        this.f$0 = securityWarningPreference;
        this.f$1 = bundle;
        this.f$2 = settingsPreferenceFragment;
    }

    public final void onClick(View view) {
        this.f$0.lambda$setSecurityWarning$3(this.f$1, this.f$2, view);
    }
}
