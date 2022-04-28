package com.google.android.settings.security;

import com.google.android.settings.security.SecurityContentManager;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityHubDashboard$$ExternalSyntheticLambda6 implements Function {
    public static final /* synthetic */ SecurityHubDashboard$$ExternalSyntheticLambda6 INSTANCE = new SecurityHubDashboard$$ExternalSyntheticLambda6();

    private /* synthetic */ SecurityHubDashboard$$ExternalSyntheticLambda6() {
    }

    public final Object apply(Object obj) {
        return ((SecurityContentManager.Entry) obj).getSecuritySourceId();
    }
}
