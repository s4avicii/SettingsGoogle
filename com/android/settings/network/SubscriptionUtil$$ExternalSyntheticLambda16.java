package com.android.settings.network;

import android.content.Context;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda16 implements Supplier {
    public final /* synthetic */ Supplier f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ Context f$2;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda16(Supplier supplier, Set set, Context context) {
        this.f$0 = supplier;
        this.f$1 = set;
        this.f$2 = context;
    }

    public final Object get() {
        return ((Stream) this.f$0.get()).map(new SubscriptionUtil$$ExternalSyntheticLambda2(this.f$1, this.f$2));
    }
}
