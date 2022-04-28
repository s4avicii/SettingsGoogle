package com.google.android.settings.fuelgauge;

import android.content.Context;
import android.os.ResultReceiver;
import com.android.settingslib.bluetooth.LocalBluetoothManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ ResultReceiver f$1;
    public final /* synthetic */ LocalBluetoothManager f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda0(Context context, ResultReceiver resultReceiver, LocalBluetoothManager localBluetoothManager, boolean z) {
        this.f$0 = context;
        this.f$1 = resultReceiver;
        this.f$2 = localBluetoothManager;
        this.f$3 = z;
    }

    public final void run() {
        BluetoothBatteryMetadataFetcher.sendAndFilterBluetoothData(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
