package com.android.settings.network;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda10 implements Predicate {
    public static final /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda10 INSTANCE = new NetworkProviderSettings$$ExternalSyntheticLambda10();

    private /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda10() {
    }

    public final boolean test(Object obj) {
        return NetworkProviderSettings.lambda$onWifiEntriesChanged$7((WifiEntry) obj);
    }
}
