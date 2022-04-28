package com.android.settings.network.telephony;

import android.telephony.CellInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NetworkSelectSettings$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ Class f$1;

    public /* synthetic */ NetworkSelectSettings$$ExternalSyntheticLambda1(String str, Class cls) {
        this.f$0 = str;
        this.f$1 = cls;
    }

    public final boolean test(Object obj) {
        return NetworkSelectSettings.lambda$doAggregation$1(this.f$0, this.f$1, (CellInfo) obj);
    }
}
