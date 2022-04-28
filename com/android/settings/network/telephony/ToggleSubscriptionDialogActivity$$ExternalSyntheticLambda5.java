package com.android.settings.network.telephony;

import android.telephony.UiccSlotInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda5 implements Predicate {
    public static final /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda5 INSTANCE = new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda5();

    private /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda5() {
    }

    public final boolean test(Object obj) {
        return ToggleSubscriptionDialogActivity.lambda$isRemovableSimEnabled$4((UiccSlotInfo) obj);
    }
}
