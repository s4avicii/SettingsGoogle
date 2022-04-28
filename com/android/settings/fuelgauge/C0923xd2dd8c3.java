package com.android.settings.fuelgauge;

import com.android.settings.fuelgauge.BatteryChartPreferenceController;
import java.util.Map;

/* renamed from: com.android.settings.fuelgauge.BatteryChartPreferenceController$LoadAllItemsInfoTask$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0923xd2dd8c3 implements Runnable {
    public final /* synthetic */ BatteryChartPreferenceController.LoadAllItemsInfoTask f$0;
    public final /* synthetic */ Map f$1;

    public /* synthetic */ C0923xd2dd8c3(BatteryChartPreferenceController.LoadAllItemsInfoTask loadAllItemsInfoTask, Map map) {
        this.f$0 = loadAllItemsInfoTask;
        this.f$1 = map;
    }

    public final void run() {
        this.f$0.lambda$onPostExecute$1(this.f$1);
    }
}
