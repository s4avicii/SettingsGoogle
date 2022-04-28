package com.android.settings.datausage;

import com.android.settings.datausage.DataSaverBackend;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DataSaverBackend$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DataSaverBackend.C08011 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ DataSaverBackend$1$$ExternalSyntheticLambda0(DataSaverBackend.C08011 r1, int i, int i2) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        this.f$0.lambda$onUidPoliciesChanged$0(this.f$1, this.f$2);
    }
}
