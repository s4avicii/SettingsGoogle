package com.android.settings.network;

import android.content.pm.UserInfo;
import android.net.VpnManager;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VpnPreferenceController$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ VpnManager f$0;

    public /* synthetic */ VpnPreferenceController$$ExternalSyntheticLambda1(VpnManager vpnManager) {
        this.f$0 = vpnManager;
    }

    public final Object apply(Object obj) {
        return this.f$0.getVpnConfig(((UserInfo) obj).id);
    }
}
