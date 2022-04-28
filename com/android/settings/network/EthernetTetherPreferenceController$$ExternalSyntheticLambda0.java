package com.android.settings.network;

import android.net.EthernetManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class EthernetTetherPreferenceController$$ExternalSyntheticLambda0 implements EthernetManager.Listener {
    public final /* synthetic */ EthernetTetherPreferenceController f$0;

    public /* synthetic */ EthernetTetherPreferenceController$$ExternalSyntheticLambda0(EthernetTetherPreferenceController ethernetTetherPreferenceController) {
        this.f$0 = ethernetTetherPreferenceController;
    }

    public final void onAvailabilityChanged(String str, boolean z) {
        this.f$0.lambda$onStart$0(str, z);
    }
}
