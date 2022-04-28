package com.google.android.settings.fuelgauge;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

final class BluetoothBatteryMetadataFetcher {
    @VisibleForTesting
    static LocalBluetoothManager sLocalBluetoothManager;

    static void returnBluetoothDevices(Context context, Intent intent) {
        ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
        if (resultReceiver == null) {
            Log.w("BluetoothBatteryMetadataFetcher", "No result receiver found from intent");
            return;
        }
        LocalBluetoothManager localBluetoothManager = sLocalBluetoothManager;
        if (localBluetoothManager == null) {
            localBluetoothManager = LocalBluetoothManager.getInstance(context, (LocalBluetoothManager.BluetoothManagerCallback) null);
        }
        BluetoothAdapter adapter = ((BluetoothManager) context.getSystemService(BluetoothManager.class)).getAdapter();
        if (adapter == null || !adapter.isEnabled() || localBluetoothManager == null) {
            Log.w("BluetoothBatteryMetadataFetcher", "BluetoothAdapter not present or not enabled");
            resultReceiver.send(1, (Bundle) null);
            return;
        }
        AsyncTask.execute(new BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda0(context, resultReceiver, localBluetoothManager, intent.getBooleanExtra("extra_fetch_icon", false)));
    }

    /* access modifiers changed from: package-private */
    public static void sendAndFilterBluetoothData(Context context, ResultReceiver resultReceiver, LocalBluetoothManager localBluetoothManager, boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        Collection<CachedBluetoothDevice> cachedDevicesCopy = localBluetoothManager.getCachedDeviceManager().getCachedDevicesCopy();
        Log.d("BluetoothBatteryMetadataFetcher", "cachedDevices:" + cachedDevicesCopy);
        if (cachedDevicesCopy == null || cachedDevicesCopy.isEmpty()) {
            resultReceiver.send(0, Bundle.EMPTY);
            return;
        }
        List<CachedBluetoothDevice> list = (List) cachedDevicesCopy.stream().filter(BluetoothBatteryMetadataFetcher$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
        Log.d("BluetoothBatteryMetadataFetcher", "connectedDevices:" + list);
        if (list.isEmpty()) {
            resultReceiver.send(0, Bundle.EMPTY);
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (CachedBluetoothDevice cachedBluetoothDevice : list) {
            BluetoothDevice device = cachedBluetoothDevice.getDevice();
            arrayList2.add(device);
            try {
                arrayList.add(BluetoothUtils.wrapBluetoothData(context, cachedBluetoothDevice, z));
            } catch (Exception e) {
                Log.e("BluetoothBatteryMetadataFetcher", "wrapBluetoothData() failed: " + device, e);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("bluetoothParcelableListKey", arrayList2);
        if (!arrayList.isEmpty()) {
            bundle.putParcelableArrayList("bluetoothWrapDataListKey", arrayList);
        }
        resultReceiver.send(0, bundle);
        Log.d("BluetoothBatteryMetadataFetcher", String.format("sendAndFilterBluetoothData() size=%d in %d/ms", new Object[]{Integer.valueOf(arrayList.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis)}));
    }
}
