package com.android.settings.network;

import android.os.Handler;
import java.util.concurrent.Executor;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class EthernetTetherPreferenceController$$ExternalSyntheticLambda1 implements Executor {
    public final /* synthetic */ Handler f$0;

    public /* synthetic */ EthernetTetherPreferenceController$$ExternalSyntheticLambda1(Handler handler) {
        this.f$0 = handler;
    }

    public final void execute(Runnable runnable) {
        this.f$0.post(runnable);
    }
}
