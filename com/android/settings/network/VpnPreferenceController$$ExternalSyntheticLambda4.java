package com.android.settings.network;

import com.android.internal.net.VpnProfile;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VpnPreferenceController$$ExternalSyntheticLambda4 implements Predicate {
    public static final /* synthetic */ VpnPreferenceController$$ExternalSyntheticLambda4 INSTANCE = new VpnPreferenceController$$ExternalSyntheticLambda4();

    private /* synthetic */ VpnPreferenceController$$ExternalSyntheticLambda4() {
    }

    public final boolean test(Object obj) {
        return VpnProfile.isLegacyType(((VpnProfile) obj).type);
    }
}
