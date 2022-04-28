package com.google.android.settings.security;

import com.google.android.settings.security.SecurityContentManager;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityContentManager$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ SecurityContentManager f$0;

    public /* synthetic */ SecurityContentManager$$ExternalSyntheticLambda1(SecurityContentManager securityContentManager) {
        this.f$0 = securityContentManager;
    }

    public final void accept(Object obj) {
        this.f$0.notifySubscriberIfStarted((SecurityContentManager.UiDataSubscriber) obj);
    }
}
