package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiPickerTracker;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiPickerTracker.WifiPickerTrackerCallback f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda0(WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        this.f$0 = wifiPickerTrackerCallback;
    }

    public final void run() {
        this.f$0.onNumSavedNetworksChanged();
    }
}
