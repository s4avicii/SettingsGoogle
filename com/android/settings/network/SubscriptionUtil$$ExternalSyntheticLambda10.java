package com.android.settings.network;

import android.os.ParcelUuid;
import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda10 implements Predicate {
    public final /* synthetic */ ParcelUuid f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda10(ParcelUuid parcelUuid) {
        this.f$0 = parcelUuid;
    }

    public final boolean test(Object obj) {
        return SubscriptionUtil.lambda$findAllSubscriptionsInGroup$13(this.f$0, (SubscriptionInfo) obj);
    }
}
