package com.google.android.settings.security;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityHubDashboard$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ PreferenceScreen f$0;

    public /* synthetic */ SecurityHubDashboard$$ExternalSyntheticLambda3(PreferenceScreen preferenceScreen) {
        this.f$0 = preferenceScreen;
    }

    public final void accept(Object obj) {
        this.f$0.removePreference((Preference) obj);
    }
}
