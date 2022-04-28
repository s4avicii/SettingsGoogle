package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionsPreferenceController$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ SubscriptionsPreferenceController f$0;

    public /* synthetic */ SubscriptionsPreferenceController$$ExternalSyntheticLambda2(SubscriptionsPreferenceController subscriptionsPreferenceController) {
        this.f$0 = subscriptionsPreferenceController;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$isAvailable$2((SubscriptionInfo) obj);
    }
}
