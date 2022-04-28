package com.android.settings.network.helper;

import android.telephony.UiccPortInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QuerySimSlotIndex$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ QuerySimSlotIndex f$0;

    public /* synthetic */ QuerySimSlotIndex$$ExternalSyntheticLambda2(QuerySimSlotIndex querySimSlotIndex) {
        this.f$0 = querySimSlotIndex;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$mapToLogicalSlotIndex$2((UiccPortInfo) obj);
    }
}
