package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0 INSTANCE = new SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0();

    private /* synthetic */ SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0() {
    }

    public final boolean test(Object obj) {
        return ((SubscriptionInfo) obj).isEmbedded();
    }
}
