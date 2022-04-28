package com.android.settings.sim;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SelectSpecificDataSimDialogFragment$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ SubscriptionInfo f$0;

    public /* synthetic */ SelectSpecificDataSimDialogFragment$$ExternalSyntheticLambda0(SubscriptionInfo subscriptionInfo) {
        this.f$0 = subscriptionInfo;
    }

    public final boolean test(Object obj) {
        return SelectSpecificDataSimDialogFragment.lambda$getNonDefaultDataSubscriptionInfo$0(this.f$0, (SubscriptionInfo) obj);
    }
}
