package com.android.settings.network;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NetworkProviderSettings$$ExternalSyntheticLambda9 implements Predicate {
    public final /* synthetic */ NetworkProviderSettings f$0;

    public /* synthetic */ NetworkProviderSettings$$ExternalSyntheticLambda9(NetworkProviderSettings networkProviderSettings) {
        this.f$0 = networkProviderSettings;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$onWifiEntriesChanged$6((WifiEntry) obj);
    }
}
