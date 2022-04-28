package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda2 implements ToIntFunction {
    public static final /* synthetic */ SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda2 INSTANCE = new SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda2();

    private /* synthetic */ SwitchToEuiccSubscriptionSidecar$$ExternalSyntheticLambda2() {
    }

    public final int applyAsInt(Object obj) {
        return ((SubscriptionInfo) obj).getPortIndex();
    }
}
