package com.google.android.settings.security;

import com.google.android.settings.security.SecurityContentManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityContentManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SecurityContentManager f$0;
    public final /* synthetic */ SecurityContentManager.UiDataSubscriber f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ SecurityContentManager$$ExternalSyntheticLambda0(SecurityContentManager securityContentManager, SecurityContentManager.UiDataSubscriber uiDataSubscriber, boolean z) {
        this.f$0 = securityContentManager;
        this.f$1 = uiDataSubscriber;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$fetchUiDataAsync$0(this.f$1, this.f$2);
    }
}
