package com.android.settings.network;

import android.security.LegacyVpnProfileStore;
import com.android.internal.net.VpnProfile;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VpnPreferenceController$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ VpnPreferenceController$$ExternalSyntheticLambda2 INSTANCE = new VpnPreferenceController$$ExternalSyntheticLambda2();

    private /* synthetic */ VpnPreferenceController$$ExternalSyntheticLambda2() {
    }

    public final Object apply(Object obj) {
        return VpnProfile.decode((String) obj, LegacyVpnProfileStore.get("VPN_" + ((String) obj)));
    }
}
