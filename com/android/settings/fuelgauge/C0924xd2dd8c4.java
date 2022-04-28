package com.android.settings.fuelgauge;

import java.util.function.Consumer;

/* renamed from: com.android.settings.fuelgauge.BatteryChartPreferenceController$LoadAllItemsInfoTask$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0924xd2dd8c4 implements Consumer {
    public static final /* synthetic */ C0924xd2dd8c4 INSTANCE = new C0924xd2dd8c4();

    private /* synthetic */ C0924xd2dd8c4() {
    }

    public final void accept(Object obj) {
        ((BatteryDiffEntry) obj).loadLabelAndIcon();
    }
}
