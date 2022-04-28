package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda8 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda8(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return SubscriptionUtil.lambda$getSubById$12(this.f$0, (SubscriptionInfo) obj);
    }
}
