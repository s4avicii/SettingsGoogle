package com.android.settings.sim.receivers;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SimSlotChangeHandler$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ SimSlotChangeHandler$$ExternalSyntheticLambda0 INSTANCE = new SimSlotChangeHandler$$ExternalSyntheticLambda0();

    private /* synthetic */ SimSlotChangeHandler$$ExternalSyntheticLambda0() {
    }

    public final boolean test(Object obj) {
        return ((SubscriptionInfo) obj).isEmbedded();
    }
}
