package com.google.android.settings.security;

import com.google.android.settings.security.SecurityContentManager;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityHubDashboard$$ExternalSyntheticLambda4 implements Function {
    public final /* synthetic */ SecurityHubDashboard f$0;

    public /* synthetic */ SecurityHubDashboard$$ExternalSyntheticLambda4(SecurityHubDashboard securityHubDashboard) {
        this.f$0 = securityHubDashboard;
    }

    public final Object apply(Object obj) {
        return this.f$0.lambda$updateSecurityEntries$1((SecurityContentManager.Entry) obj);
    }
}
