package com.android.settings.network;

import android.telephony.UiccSlotMapping;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UiccSlotUtil$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda0(int i, int i2, int i3, int i4) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = i3;
        this.f$3 = i4;
    }

    public final Object apply(Object obj) {
        return UiccSlotUtil.lambda$switchToEuiccSlot$0(this.f$0, this.f$1, this.f$2, this.f$3, (UiccSlotMapping) obj);
    }
}
