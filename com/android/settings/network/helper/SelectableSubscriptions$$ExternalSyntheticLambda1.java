package com.android.settings.network.helper;

import android.telephony.SubscriptionManager;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SelectableSubscriptions$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ SelectableSubscriptions$$ExternalSyntheticLambda1 INSTANCE = new SelectableSubscriptions$$ExternalSyntheticLambda1();

    private /* synthetic */ SelectableSubscriptions$$ExternalSyntheticLambda1() {
    }

    public final Object apply(Object obj) {
        return ((SubscriptionManager) obj).getActiveSubscriptionInfoList();
    }
}
