package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CsipDeviceManager {
    private final LocalBluetoothManager mBtManager;
    private final List<CachedBluetoothDevice> mCachedDevices;

    private boolean isValidGroupId(int i) {
        return i != -1;
    }

    CsipDeviceManager(LocalBluetoothManager localBluetoothManager, List<CachedBluetoothDevice> list) {
        this.mBtManager = localBluetoothManager;
        this.mCachedDevices = list;
    }

    /* access modifiers changed from: package-private */
    public void initCsipDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        int baseGroupId = getBaseGroupId(cachedBluetoothDevice.getDevice());
        if (isValidGroupId(baseGroupId)) {
            log("initCsipDeviceIfNeeded: " + cachedBluetoothDevice + " (group: " + baseGroupId + ")");
            cachedBluetoothDevice.setGroupId(baseGroupId);
        }
    }

    private int getBaseGroupId(BluetoothDevice bluetoothDevice) {
        Map<Integer, ParcelUuid> groupUuidMapByDevice;
        CsipSetCoordinatorProfile csipSetCoordinatorProfile = this.mBtManager.getProfileManager().getCsipSetCoordinatorProfile();
        if (csipSetCoordinatorProfile == null || (groupUuidMapByDevice = csipSetCoordinatorProfile.getGroupUuidMapByDevice(bluetoothDevice)) == null) {
            return -1;
        }
        for (Map.Entry next : groupUuidMapByDevice.entrySet()) {
            if (((ParcelUuid) next.getValue()).equals(BluetoothUuid.CAP)) {
                return ((Integer) next.getKey()).intValue();
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public boolean setMemberDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        int groupId = cachedBluetoothDevice.getGroupId();
        if (!isValidGroupId(groupId)) {
            return false;
        }
        CachedBluetoothDevice cachedDevice = getCachedDevice(groupId);
        log("setMemberDeviceIfNeeded, main: " + cachedDevice + ", member: " + cachedBluetoothDevice);
        if (cachedDevice == null) {
            return false;
        }
        cachedDevice.setMemberDevice(cachedBluetoothDevice);
        cachedBluetoothDevice.setName(cachedDevice.getName());
        return true;
    }

    private CachedBluetoothDevice getCachedDevice(int i) {
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
            if (cachedBluetoothDevice.getGroupId() == i) {
                return cachedBluetoothDevice;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void updateCsipDevices() {
        HashSet<Integer> hashSet = new HashSet<>();
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (!isValidGroupId(next.getGroupId())) {
                int baseGroupId = getBaseGroupId(next.getDevice());
                if (isValidGroupId(baseGroupId)) {
                    next.setGroupId(baseGroupId);
                    hashSet.add(Integer.valueOf(baseGroupId));
                }
            }
        }
        for (Integer intValue : hashSet) {
            onGroupIdChanged(intValue.intValue());
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void onGroupIdChanged(int i) {
        CachedBluetoothDevice cachedBluetoothDevice = null;
        int i2 = -1;
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice2 = this.mCachedDevices.get(size);
            if (cachedBluetoothDevice2.getGroupId() == i) {
                if (i2 == -1) {
                    i2 = size;
                    cachedBluetoothDevice = cachedBluetoothDevice2;
                } else {
                    log("onGroupIdChanged: removed from UI device =" + cachedBluetoothDevice2 + ", with groupId=" + i + " firstMatchedIndex=" + i2);
                    cachedBluetoothDevice.setMemberDevice(cachedBluetoothDevice2);
                    this.mCachedDevices.remove(size);
                    this.mBtManager.getEventManager().dispatchDeviceRemoved(cachedBluetoothDevice2);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onProfileConnectionStateChangedIfProcessed(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        log("onProfileConnectionStateChangedIfProcessed: " + cachedBluetoothDevice + ", state: " + i);
        if (i == 0) {
            CachedBluetoothDevice findMainDevice = findMainDevice(cachedBluetoothDevice);
            if (findMainDevice != null) {
                findMainDevice.refresh();
                return true;
            }
            Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
            if (memberDevice.isEmpty()) {
                return false;
            }
            for (CachedBluetoothDevice next : memberDevice) {
                if (next.isConnected()) {
                    this.mBtManager.getEventManager().dispatchDeviceRemoved(cachedBluetoothDevice);
                    cachedBluetoothDevice.switchMemberDeviceContent(next, cachedBluetoothDevice);
                    cachedBluetoothDevice.refresh();
                    this.mBtManager.getEventManager().dispatchDeviceAdded(cachedBluetoothDevice);
                    return true;
                }
            }
            return false;
        } else if (i != 2) {
            return false;
        } else {
            onGroupIdChanged(cachedBluetoothDevice.getGroupId());
            CachedBluetoothDevice findMainDevice2 = findMainDevice(cachedBluetoothDevice);
            if (findMainDevice2 == null) {
                return false;
            }
            if (findMainDevice2.isConnected()) {
                findMainDevice2.refresh();
                return true;
            }
            this.mBtManager.getEventManager().dispatchDeviceRemoved(findMainDevice2);
            findMainDevice2.switchMemberDeviceContent(findMainDevice2, cachedBluetoothDevice);
            findMainDevice2.refresh();
            this.mBtManager.getEventManager().dispatchDeviceAdded(findMainDevice2);
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice findMainDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        List<CachedBluetoothDevice> list;
        if (!(cachedBluetoothDevice == null || (list = this.mCachedDevices) == null)) {
            for (CachedBluetoothDevice next : list) {
                if (isValidGroupId(next.getGroupId())) {
                    Set<CachedBluetoothDevice> memberDevice = next.getMemberDevice();
                    if (memberDevice.isEmpty()) {
                        continue;
                    } else {
                        for (CachedBluetoothDevice next2 : memberDevice) {
                            if (next2 != null && next2.equals(cachedBluetoothDevice)) {
                                return next;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public boolean isExistedGroupId(int i) {
        return getCachedDevice(i) != null;
    }

    private void log(String str) {
        Log.d("CsipDeviceManager", str);
    }
}
