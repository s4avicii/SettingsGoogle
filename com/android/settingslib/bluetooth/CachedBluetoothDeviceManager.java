package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CachedBluetoothDeviceManager {
    private final LocalBluetoothManager mBtManager;
    @VisibleForTesting
    final List<CachedBluetoothDevice> mCachedDevices;
    private Context mContext;
    @VisibleForTesting
    CsipDeviceManager mCsipDeviceManager;
    @VisibleForTesting
    HearingAidDeviceManager mHearingAidDeviceManager;
    BluetoothDevice mOngoingSetMemberPair;

    CachedBluetoothDeviceManager(Context context, LocalBluetoothManager localBluetoothManager) {
        ArrayList arrayList = new ArrayList();
        this.mCachedDevices = arrayList;
        this.mContext = context;
        this.mBtManager = localBluetoothManager;
        this.mHearingAidDeviceManager = new HearingAidDeviceManager(localBluetoothManager, arrayList);
        this.mCsipDeviceManager = new CsipDeviceManager(localBluetoothManager, arrayList);
    }

    public synchronized Collection<CachedBluetoothDevice> getCachedDevicesCopy() {
        return new ArrayList(this.mCachedDevices);
    }

    public void onDeviceNameUpdated(BluetoothDevice bluetoothDevice) {
        CachedBluetoothDevice findDevice = findDevice(bluetoothDevice);
        if (findDevice != null) {
            findDevice.refreshName();
        }
    }

    public synchronized CachedBluetoothDevice findDevice(BluetoothDevice bluetoothDevice) {
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (next.getDevice().equals(bluetoothDevice)) {
                return next;
            }
            Set<CachedBluetoothDevice> memberDevice = next.getMemberDevice();
            if (!memberDevice.isEmpty()) {
                for (CachedBluetoothDevice next2 : memberDevice) {
                    if (next2.getDevice().equals(bluetoothDevice)) {
                        return next2;
                    }
                }
            }
            CachedBluetoothDevice subDevice = next.getSubDevice();
            if (subDevice != null && subDevice.getDevice().equals(bluetoothDevice)) {
                return subDevice;
            }
        }
        return null;
    }

    public CachedBluetoothDevice addDevice(BluetoothDevice bluetoothDevice) {
        CachedBluetoothDevice findDevice;
        LocalBluetoothProfileManager profileManager = this.mBtManager.getProfileManager();
        synchronized (this) {
            findDevice = findDevice(bluetoothDevice);
            if (findDevice == null) {
                findDevice = new CachedBluetoothDevice(this.mContext, profileManager, bluetoothDevice);
                this.mCsipDeviceManager.initCsipDeviceIfNeeded(findDevice);
                this.mHearingAidDeviceManager.initHearingAidDeviceIfNeeded(findDevice);
                if (!this.mCsipDeviceManager.setMemberDeviceIfNeeded(findDevice) && !this.mHearingAidDeviceManager.setSubDeviceIfNeeded(findDevice)) {
                    this.mCachedDevices.add(findDevice);
                    this.mBtManager.getEventManager().dispatchDeviceAdded(findDevice);
                }
            }
        }
        return findDevice;
    }

    public synchronized String getSubDeviceSummary(CachedBluetoothDevice cachedBluetoothDevice) {
        Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
        if (!memberDevice.isEmpty()) {
            for (CachedBluetoothDevice next : memberDevice) {
                if (next.isConnected()) {
                    return next.getConnectionSummary();
                }
            }
        }
        CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
        if (subDevice == null || !subDevice.isConnected()) {
            return null;
        }
        return subDevice.getConnectionSummary();
    }

    public synchronized boolean isSubDevice(BluetoothDevice bluetoothDevice) {
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (!next.getDevice().equals(bluetoothDevice)) {
                Set<CachedBluetoothDevice> memberDevice = next.getMemberDevice();
                if (!memberDevice.isEmpty()) {
                    for (CachedBluetoothDevice device : memberDevice) {
                        if (device.getDevice().equals(bluetoothDevice)) {
                            return true;
                        }
                    }
                    continue;
                } else {
                    CachedBluetoothDevice subDevice = next.getSubDevice();
                    if (subDevice != null && subDevice.getDevice().equals(bluetoothDevice)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public synchronized void updateHearingAidsDevices() {
        this.mHearingAidDeviceManager.updateHearingAidsDevices();
    }

    public synchronized void updateCsipDevices() {
        this.mCsipDeviceManager.updateCsipDevices();
    }

    public String getName(BluetoothDevice bluetoothDevice) {
        CachedBluetoothDevice findDevice = findDevice(bluetoothDevice);
        if (findDevice != null && findDevice.getName() != null) {
            return findDevice.getName();
        }
        String alias = bluetoothDevice.getAlias();
        if (alias != null) {
            return alias;
        }
        return bluetoothDevice.getAddress();
    }

    public synchronized void clearNonBondedDevices() {
        clearNonBondedSubDevices();
        this.mCachedDevices.removeIf(CachedBluetoothDeviceManager$$ExternalSyntheticLambda0.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$clearNonBondedDevices$0(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.getBondState() == 10;
    }

    private void clearNonBondedSubDevices() {
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
            Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
            if (!memberDevice.isEmpty()) {
                for (Object obj : memberDevice.toArray()) {
                    CachedBluetoothDevice cachedBluetoothDevice2 = (CachedBluetoothDevice) obj;
                    if (cachedBluetoothDevice2.getDevice().getBondState() == 10) {
                        cachedBluetoothDevice.removeMemberDevice(cachedBluetoothDevice2);
                    }
                }
                return;
            }
            CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
            if (subDevice != null && subDevice.getDevice().getBondState() == 10) {
                cachedBluetoothDevice.setSubDevice((CachedBluetoothDevice) null);
            }
        }
    }

    public synchronized void onScanningStateChanged(boolean z) {
        if (z) {
            for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
                CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
                cachedBluetoothDevice.setJustDiscovered(false);
                Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
                if (!memberDevice.isEmpty()) {
                    for (CachedBluetoothDevice justDiscovered : memberDevice) {
                        justDiscovered.setJustDiscovered(false);
                    }
                    return;
                }
                CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
                if (subDevice != null) {
                    subDevice.setJustDiscovered(false);
                }
            }
        }
    }

    public synchronized void onBluetoothStateChanged(int i) {
        if (i == 13) {
            for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
                CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
                Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
                if (!memberDevice.isEmpty()) {
                    for (CachedBluetoothDevice next : memberDevice) {
                        if (next.getBondState() != 12) {
                            cachedBluetoothDevice.removeMemberDevice(next);
                        }
                    }
                } else {
                    CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
                    if (!(subDevice == null || subDevice.getBondState() == 12)) {
                        cachedBluetoothDevice.setSubDevice((CachedBluetoothDevice) null);
                    }
                }
                if (cachedBluetoothDevice.getBondState() != 12) {
                    cachedBluetoothDevice.setJustDiscovered(false);
                    this.mCachedDevices.remove(size);
                }
            }
        }
    }

    public synchronized boolean onProfileConnectionStateChangedIfProcessed(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (i2 == 21) {
            return this.mHearingAidDeviceManager.onProfileConnectionStateChangedIfProcessed(cachedBluetoothDevice, i);
        } else if (i2 != 25) {
            return false;
        } else {
            return this.mCsipDeviceManager.onProfileConnectionStateChangedIfProcessed(cachedBluetoothDevice, i);
        }
    }

    public synchronized void onDeviceUnpaired(CachedBluetoothDevice cachedBluetoothDevice) {
        CachedBluetoothDevice findMainDevice = this.mCsipDeviceManager.findMainDevice(cachedBluetoothDevice);
        Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
        if (!memberDevice.isEmpty()) {
            for (CachedBluetoothDevice next : memberDevice) {
                next.unpair();
                cachedBluetoothDevice.removeMemberDevice(next);
            }
        } else if (findMainDevice != null) {
            findMainDevice.unpair();
        }
        CachedBluetoothDevice findMainDevice2 = this.mHearingAidDeviceManager.findMainDevice(cachedBluetoothDevice);
        CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
        if (subDevice != null) {
            subDevice.unpair();
            cachedBluetoothDevice.setSubDevice((CachedBluetoothDevice) null);
        } else if (findMainDevice2 != null) {
            findMainDevice2.unpair();
            findMainDevice2.setSubDevice((CachedBluetoothDevice) null);
        }
    }

    public synchronized boolean shouldPairByCsip(BluetoothDevice bluetoothDevice, int i) {
        if (this.mOngoingSetMemberPair == null && bluetoothDevice.getBondState() == 10) {
            if (this.mCsipDeviceManager.isExistedGroupId(i)) {
                Log.d("CachedBluetoothDeviceManager", "Bond " + bluetoothDevice.getName() + " by CSIP");
                this.mOngoingSetMemberPair = bluetoothDevice;
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003a, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean onBondStateChangedIfProcess(android.bluetooth.BluetoothDevice r4, int r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            android.bluetooth.BluetoothDevice r0 = r3.mOngoingSetMemberPair     // Catch:{ all -> 0x003e }
            if (r0 == 0) goto L_0x003b
            boolean r0 = r0.equals(r4)     // Catch:{ all -> 0x003e }
            if (r0 != 0) goto L_0x000c
            goto L_0x003b
        L_0x000c:
            r0 = 11
            r1 = 1
            if (r5 != r0) goto L_0x0013
            monitor-exit(r3)
            return r1
        L_0x0013:
            r0 = 0
            r3.mOngoingSetMemberPair = r0     // Catch:{ all -> 0x003e }
            r0 = 10
            if (r5 == r0) goto L_0x0039
            com.android.settingslib.bluetooth.CachedBluetoothDevice r5 = r3.findDevice(r4)     // Catch:{ all -> 0x003e }
            if (r5 != 0) goto L_0x0039
            com.android.settingslib.bluetooth.LocalBluetoothManager r5 = r3.mBtManager     // Catch:{ all -> 0x003e }
            com.android.settingslib.bluetooth.LocalBluetoothProfileManager r5 = r5.getProfileManager()     // Catch:{ all -> 0x003e }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r0 = new com.android.settingslib.bluetooth.CachedBluetoothDevice     // Catch:{ all -> 0x003e }
            android.content.Context r2 = r3.mContext     // Catch:{ all -> 0x003e }
            r0.<init>(r2, r5, r4)     // Catch:{ all -> 0x003e }
            java.util.List<com.android.settingslib.bluetooth.CachedBluetoothDevice> r5 = r3.mCachedDevices     // Catch:{ all -> 0x003e }
            r5.add(r0)     // Catch:{ all -> 0x003e }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = r3.findDevice(r4)     // Catch:{ all -> 0x003e }
            r4.connect()     // Catch:{ all -> 0x003e }
        L_0x0039:
            monitor-exit(r3)
            return r1
        L_0x003b:
            r4 = 0
            monitor-exit(r3)
            return r4
        L_0x003e:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDeviceManager.onBondStateChangedIfProcess(android.bluetooth.BluetoothDevice, int):boolean");
    }

    public boolean isOngoingPairByCsip(BluetoothDevice bluetoothDevice) {
        BluetoothDevice bluetoothDevice2 = this.mOngoingSetMemberPair;
        return bluetoothDevice2 != null && bluetoothDevice2.equals(bluetoothDevice);
    }
}
