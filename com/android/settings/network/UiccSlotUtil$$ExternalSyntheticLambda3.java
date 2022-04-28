package com.android.settings.network;

import android.telephony.UiccSlotMapping;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UiccSlotUtil$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda3(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return UiccSlotUtil.lambda$prepareUiccSlotMappingsForRemovableSlot$3(this.f$0, (UiccSlotMapping) obj);
    }
}
