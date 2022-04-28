package com.android.settings.wifi.dpp;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiNetworkListFragment$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ WifiNetworkListFragment f$0;

    public /* synthetic */ WifiNetworkListFragment$$ExternalSyntheticLambda0(WifiNetworkListFragment wifiNetworkListFragment) {
        this.f$0 = wifiNetworkListFragment;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$onSavedWifiEntriesChanged$0((WifiEntry) obj);
    }
}
