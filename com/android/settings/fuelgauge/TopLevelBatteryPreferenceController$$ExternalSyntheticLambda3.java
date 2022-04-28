package com.android.settings.fuelgauge;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TopLevelBatteryPreferenceController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ TopLevelBatteryPreferenceController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ BatteryInfo f$2;

    public /* synthetic */ TopLevelBatteryPreferenceController$$ExternalSyntheticLambda3(TopLevelBatteryPreferenceController topLevelBatteryPreferenceController, boolean z, BatteryInfo batteryInfo) {
        this.f$0 = topLevelBatteryPreferenceController;
        this.f$1 = z;
        this.f$2 = batteryInfo;
    }

    public final void run() {
        this.f$0.lambda$setSummaryAsync$2(this.f$1, this.f$2);
    }
}
