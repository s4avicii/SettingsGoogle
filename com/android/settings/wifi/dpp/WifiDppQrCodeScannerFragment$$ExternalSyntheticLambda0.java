package com.android.settings.wifi.dpp;

import androidx.lifecycle.Observer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WifiDppQrCodeScannerFragment$$ExternalSyntheticLambda0 implements Observer {
    public final /* synthetic */ WifiDppQrCodeScannerFragment f$0;
    public final /* synthetic */ WifiDppInitiatorViewModel f$1;

    public /* synthetic */ WifiDppQrCodeScannerFragment$$ExternalSyntheticLambda0(WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment, WifiDppInitiatorViewModel wifiDppInitiatorViewModel) {
        this.f$0 = wifiDppQrCodeScannerFragment;
        this.f$1 = wifiDppInitiatorViewModel;
    }

    public final void onChanged(Object obj) {
        this.f$0.lambda$onCreate$1(this.f$1, (Integer) obj);
    }
}
