package com.android.settings.network;

import com.android.settings.network.helper.SubscriptionAnnotation;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda14 implements Predicate {
    public static final /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda14 INSTANCE = new SubscriptionUtil$$ExternalSyntheticLambda14();

    private /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda14() {
    }

    public final boolean test(Object obj) {
        return ((SubscriptionAnnotation) obj).isActive();
    }
}
