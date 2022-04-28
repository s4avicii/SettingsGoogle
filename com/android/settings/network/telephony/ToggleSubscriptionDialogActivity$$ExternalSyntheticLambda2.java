package com.android.settings.network.telephony;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda2 INSTANCE = new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda2();

    private /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda2() {
    }

    public final boolean test(Object obj) {
        return ((SubscriptionInfo) obj).isEmbedded();
    }
}
