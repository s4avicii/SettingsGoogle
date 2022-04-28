package com.android.settings.wifi.calling;

import android.telephony.SubscriptionInfo;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiCallingSettings$$ExternalSyntheticLambda1 implements ToIntFunction {
    public static final /* synthetic */ WifiCallingSettings$$ExternalSyntheticLambda1 INSTANCE = new WifiCallingSettings$$ExternalSyntheticLambda1();

    private /* synthetic */ WifiCallingSettings$$ExternalSyntheticLambda1() {
    }

    public final int applyAsInt(Object obj) {
        return WifiCallingSettings.lambda$subscriptionIdList$0((SubscriptionInfo) obj);
    }
}
