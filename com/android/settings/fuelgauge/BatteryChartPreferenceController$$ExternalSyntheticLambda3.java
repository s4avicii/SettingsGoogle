package com.android.settings.fuelgauge;

import java.util.List;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BatteryChartPreferenceController$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ BatteryChartPreferenceController f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ BatteryChartPreferenceController$$ExternalSyntheticLambda3(BatteryChartPreferenceController batteryChartPreferenceController, List list) {
        this.f$0 = batteryChartPreferenceController;
        this.f$1 = list;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$addAllPreferences$2(this.f$1, (BatteryDiffEntry) obj);
    }
}
