package com.android.settings.network;

import android.content.Context;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda15 implements Supplier {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda15(Context context) {
        this.f$0 = context;
    }

    public final Object get() {
        return SubscriptionUtil.getAvailableSubscriptions(this.f$0).stream().filter(SubscriptionUtil$$ExternalSyntheticLambda13.INSTANCE).map(new SubscriptionUtil$$ExternalSyntheticLambda0(this.f$0));
    }
}
