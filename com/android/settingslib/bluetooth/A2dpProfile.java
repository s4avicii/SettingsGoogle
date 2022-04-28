package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.R$string;
import java.util.ArrayList;
import java.util.List;

public class A2dpProfile implements LocalBluetoothProfile {
    static final ParcelUuid[] SINK_UUIDS = {BluetoothUuid.A2DP_SINK, BluetoothUuid.ADV_AUDIO_DIST};
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothA2dp mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302329;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 2;
    }

    public String toString() {
        return "A2DP";
    }

    private final class A2dpServiceListener implements BluetoothProfile.ServiceListener {
        private A2dpServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            A2dpProfile.this.mService = (BluetoothA2dp) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = A2dpProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice remove = connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = A2dpProfile.this.mDeviceManager.findDevice(remove);
                if (findDevice == null) {
                    Log.w("A2dpProfile", "A2dpProfile found new device: " + remove);
                    findDevice = A2dpProfile.this.mDeviceManager.addDevice(remove);
                }
                findDevice.onProfileStateChanged(A2dpProfile.this, 2);
                findDevice.refresh();
            }
            A2dpProfile.this.mIsProfileReady = true;
            A2dpProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            A2dpProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    A2dpProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new A2dpServiceListener(), 2);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        return getDevicesByStates(new int[]{2, 1, 3});
    }

    private List<BluetoothDevice> getDevicesByStates(int[] iArr) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return new ArrayList(0);
        }
        return bluetoothA2dp.getDevicesMatchingConnectionStates(iArr);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return 0;
        }
        return bluetoothA2dp.getConnectionState(bluetoothDevice);
    }

    public boolean setActiveDevice(BluetoothDevice bluetoothDevice) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return false;
        }
        if (bluetoothDevice == null) {
            return bluetoothAdapter.removeActiveDevice(0);
        }
        return bluetoothAdapter.setActiveDevice(bluetoothDevice, 0);
    }

    public BluetoothDevice getActiveDevice() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return null;
        }
        List activeDevices = bluetoothAdapter.getActiveDevices(2);
        if (activeDevices.size() > 0) {
            return (BluetoothDevice) activeDevices.get(0);
        }
        return null;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp != null && bluetoothA2dp.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return false;
        }
        if (!z) {
            return bluetoothA2dp.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothA2dp.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public boolean supportsHighQualityAudio(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            bluetoothDevice = getActiveDevice();
        }
        if (bluetoothDevice != null && this.mService.isOptionalCodecsSupported(bluetoothDevice) == 1) {
            return true;
        }
        return false;
    }

    public boolean isHighQualityAudioEnabled(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            bluetoothDevice = getActiveDevice();
        }
        if (bluetoothDevice == null) {
            return false;
        }
        int isOptionalCodecsEnabled = this.mService.isOptionalCodecsEnabled(bluetoothDevice);
        if (isOptionalCodecsEnabled != -1) {
            if (isOptionalCodecsEnabled == 1) {
                return true;
            }
            return false;
        } else if (getConnectionStatus(bluetoothDevice) != 2 && supportsHighQualityAudio(bluetoothDevice)) {
            return true;
        } else {
            BluetoothCodecConfig bluetoothCodecConfig = null;
            if (this.mService.getCodecStatus(bluetoothDevice) != null) {
                bluetoothCodecConfig = this.mService.getCodecStatus(bluetoothDevice).getCodecConfig();
            }
            if (bluetoothCodecConfig != null) {
                return !bluetoothCodecConfig.isMandatoryCodec();
            }
            return false;
        }
    }

    public void setHighQualityAudioEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        if (bluetoothDevice == null) {
            bluetoothDevice = getActiveDevice();
        }
        if (bluetoothDevice != null) {
            this.mService.setOptionalCodecsEnabled(bluetoothDevice, z ? 1 : 0);
            if (getConnectionStatus(bluetoothDevice) == 2) {
                if (z) {
                    this.mService.enableOptionalCodecs(bluetoothDevice);
                } else {
                    this.mService.disableOptionalCodecs(bluetoothDevice);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: android.bluetooth.BluetoothCodecConfig} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getHighQualityAudioOptionLabel(android.bluetooth.BluetoothDevice r8) {
        /*
            r7 = this;
            if (r8 == 0) goto L_0x0004
            r0 = r8
            goto L_0x0008
        L_0x0004:
            android.bluetooth.BluetoothDevice r0 = r7.getActiveDevice()
        L_0x0008:
            int r1 = com.android.settingslib.R$string.bluetooth_profile_a2dp_high_quality_unknown_codec
            if (r0 == 0) goto L_0x0090
            boolean r0 = r7.supportsHighQualityAudio(r8)
            if (r0 == 0) goto L_0x0090
            int r0 = r7.getConnectionStatus(r8)
            r2 = 2
            if (r0 == r2) goto L_0x001b
            goto L_0x0090
        L_0x001b:
            android.bluetooth.BluetoothA2dp r0 = r7.mService
            android.bluetooth.BluetoothCodecStatus r0 = r0.getCodecStatus(r8)
            r3 = 0
            if (r0 == 0) goto L_0x0034
            android.bluetooth.BluetoothA2dp r0 = r7.mService
            android.bluetooth.BluetoothCodecStatus r8 = r0.getCodecStatus(r8)
            java.util.List r8 = r8.getCodecsSelectableCapabilities()
            com.android.settingslib.bluetooth.A2dpProfile$$ExternalSyntheticLambda0 r0 = com.android.settingslib.bluetooth.A2dpProfile$$ExternalSyntheticLambda0.INSTANCE
            java.util.Collections.sort(r8, r0)
            goto L_0x0035
        L_0x0034:
            r8 = r3
        L_0x0035:
            r0 = 0
            r4 = 1
            if (r8 == 0) goto L_0x0047
            int r5 = r8.size()
            if (r5 >= r4) goto L_0x0040
            goto L_0x0047
        L_0x0040:
            java.lang.Object r8 = r8.get(r0)
            r3 = r8
            android.bluetooth.BluetoothCodecConfig r3 = (android.bluetooth.BluetoothCodecConfig) r3
        L_0x0047:
            if (r3 == 0) goto L_0x0055
            boolean r8 = r3.isMandatoryCodec()
            if (r8 == 0) goto L_0x0050
            goto L_0x0055
        L_0x0050:
            int r8 = r3.getCodecType()
            goto L_0x0058
        L_0x0055:
            r8 = 1000000(0xf4240, float:1.401298E-39)
        L_0x0058:
            r3 = -1
            r5 = 4
            r6 = 3
            if (r8 == 0) goto L_0x006d
            if (r8 == r4) goto L_0x006e
            if (r8 == r2) goto L_0x006b
            if (r8 == r6) goto L_0x0069
            if (r8 == r5) goto L_0x0067
            r2 = r3
            goto L_0x006e
        L_0x0067:
            r2 = 5
            goto L_0x006e
        L_0x0069:
            r2 = r5
            goto L_0x006e
        L_0x006b:
            r2 = r6
            goto L_0x006e
        L_0x006d:
            r2 = r4
        L_0x006e:
            if (r2 >= 0) goto L_0x0077
            android.content.Context r7 = r7.mContext
            java.lang.String r7 = r7.getString(r1)
            return r7
        L_0x0077:
            android.content.Context r7 = r7.mContext
            int r8 = com.android.settingslib.R$string.bluetooth_profile_a2dp_high_quality
            java.lang.Object[] r1 = new java.lang.Object[r4]
            android.content.res.Resources r3 = r7.getResources()
            int r4 = com.android.settingslib.R$array.bluetooth_a2dp_codec_titles
            java.lang.String[] r3 = r3.getStringArray(r4)
            r2 = r3[r2]
            r1[r0] = r2
            java.lang.String r7 = r7.getString(r8, r1)
            return r7
        L_0x0090:
            android.content.Context r7 = r7.mContext
            java.lang.String r7 = r7.getString(r1)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.A2dpProfile.getHighQualityAudioOptionLabel(android.bluetooth.BluetoothDevice):java.lang.String");
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$getHighQualityAudioOptionLabel$0(BluetoothCodecConfig bluetoothCodecConfig, BluetoothCodecConfig bluetoothCodecConfig2) {
        return bluetoothCodecConfig2.getCodecPriority() - bluetoothCodecConfig.getCodecPriority();
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_a2dp;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d("A2dpProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(2, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("A2dpProfile", "Error cleaning up A2DP proxy", th);
            }
        }
    }
}
