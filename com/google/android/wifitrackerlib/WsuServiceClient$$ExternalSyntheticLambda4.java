package com.google.android.wifitrackerlib;

import com.android.wsuinterface.NetworkGroupSubscription;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WsuServiceClient$$ExternalSyntheticLambda4 implements Function {
    public final /* synthetic */ WsuServiceClient f$0;
    public final /* synthetic */ NetworkGroupSubscription f$1;

    public /* synthetic */ WsuServiceClient$$ExternalSyntheticLambda4(WsuServiceClient wsuServiceClient, NetworkGroupSubscription networkGroupSubscription) {
        this.f$0 = wsuServiceClient;
        this.f$1 = networkGroupSubscription;
    }

    public final Object apply(Object obj) {
        return this.f$0.lambda$getWsuProvider$4(this.f$1, (String) obj);
    }
}
