package com.android.settings.network.helper;

import android.telephony.UiccPortInfo;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QuerySimSlotIndex$$ExternalSyntheticLambda3 implements ToIntFunction {
    public static final /* synthetic */ QuerySimSlotIndex$$ExternalSyntheticLambda3 INSTANCE = new QuerySimSlotIndex$$ExternalSyntheticLambda3();

    private /* synthetic */ QuerySimSlotIndex$$ExternalSyntheticLambda3() {
    }

    public final int applyAsInt(Object obj) {
        return ((UiccPortInfo) obj).getLogicalSlotIndex();
    }
}
