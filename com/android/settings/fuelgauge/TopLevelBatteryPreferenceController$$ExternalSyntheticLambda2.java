package com.android.settings.fuelgauge;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TopLevelBatteryPreferenceController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ TopLevelBatteryPreferenceController f$0;
    public final /* synthetic */ BatteryInfo f$1;

    public /* synthetic */ TopLevelBatteryPreferenceController$$ExternalSyntheticLambda2(TopLevelBatteryPreferenceController topLevelBatteryPreferenceController, BatteryInfo batteryInfo) {
        this.f$0 = topLevelBatteryPreferenceController;
        this.f$1 = batteryInfo;
    }

    public final void run() {
        this.f$0.lambda$setSummaryAsync$3(this.f$1);
    }
}
