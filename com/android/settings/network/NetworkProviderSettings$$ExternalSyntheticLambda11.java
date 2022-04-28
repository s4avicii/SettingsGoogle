package com.android.settings.network;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda11 implements Predicate {
    public static final /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda11 INSTANCE = new NetworkProviderSettings$$ExternalSyntheticLambda11();

    private /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda11() {
    }

    public final boolean test(Object obj) {
        return NetworkProviderSettings.lambda$onWifiEntriesChanged$8((WifiEntry) obj);
    }
}
