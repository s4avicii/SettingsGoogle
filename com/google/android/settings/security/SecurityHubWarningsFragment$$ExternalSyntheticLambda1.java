package com.google.android.settings.security;

import android.content.Context;
import androidx.preference.PreferenceCategory;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityHubWarningsFragment$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ SecurityHubWarningsFragment f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ PreferenceCategory f$2;

    public /* synthetic */ SecurityHubWarningsFragment$$ExternalSyntheticLambda1(SecurityHubWarningsFragment securityHubWarningsFragment, Context context, PreferenceCategory preferenceCategory) {
        this.f$0 = securityHubWarningsFragment;
        this.f$1 = context;
        this.f$2 = preferenceCategory;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$updateWarningList$1(this.f$1, this.f$2, (SecurityWarning) obj);
    }
}
