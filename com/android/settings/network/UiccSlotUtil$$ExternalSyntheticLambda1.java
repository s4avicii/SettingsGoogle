package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import android.telephony.UiccSlotMapping;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UiccSlotUtil$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ SubscriptionInfo f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda1(SubscriptionInfo subscriptionInfo, int i) {
        this.f$0 = subscriptionInfo;
        this.f$1 = i;
    }

    public final Object apply(Object obj) {
        return UiccSlotUtil.lambda$prepareUiccSlotMappingsForRemovableSlot$4(this.f$0, this.f$1, (UiccSlotMapping) obj);
    }
}
