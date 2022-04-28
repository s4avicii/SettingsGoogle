package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda18 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda18 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda18();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda18() {
    }

    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateSuggestedWifiEntryScans$12((ScanResult) obj);
    }
}
