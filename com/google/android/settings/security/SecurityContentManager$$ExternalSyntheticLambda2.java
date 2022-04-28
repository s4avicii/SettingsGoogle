package com.google.android.settings.security;

import android.os.Bundle;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SecurityContentManager$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ List f$0;

    public /* synthetic */ SecurityContentManager$$ExternalSyntheticLambda2(List list) {
        this.f$0 = list;
    }

    public final void accept(Object obj) {
        this.f$0.add(SecurityContentManager.getSecurityWarningFromWarningBundle((Bundle) obj));
    }
}
