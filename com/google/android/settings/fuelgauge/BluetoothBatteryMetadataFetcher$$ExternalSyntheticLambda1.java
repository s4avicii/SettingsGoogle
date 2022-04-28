package com.google.android.settings.fuelgauge;

import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda1 INSTANCE = new BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda1();

    private /* synthetic */ BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda1() {
    }

    public final boolean test(Object obj) {
        return ((CachedBluetoothDevice) obj).isConnected();
    }
}
