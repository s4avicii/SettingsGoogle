package com.android.settings.development.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.development.BluetoothA2dpConfigStore;
import com.android.settings.development.bluetooth.BaseBluetoothDialogPreference;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.List;

public abstract class AbstractBluetoothDialogPreferenceController extends AbstractBluetoothPreferenceController implements BaseBluetoothDialogPreference.Callback {
    protected static final int[] BITS_PER_SAMPLES = {4, 2, 1};
    protected static final int[] CHANNEL_MODES = {2, 1};
    protected static final int[] CODEC_TYPES = {4, 3, 2, 1, 0};
    protected static final int[] SAMPLE_RATES = {32, 16, 8, 4, 2, 1};
    protected final BluetoothA2dpConfigStore mBluetoothA2dpConfigStore;

    /* access modifiers changed from: protected */
    public abstract int getCurrentIndexByConfig(BluetoothCodecConfig bluetoothCodecConfig);

    public void onHDAudioEnabled(boolean z) {
    }

    /* access modifiers changed from: protected */
    public abstract void writeConfigurationValues(int i);

    public AbstractBluetoothDialogPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
        this.mBluetoothA2dpConfigStore = bluetoothA2dpConfigStore;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
    }

    public CharSequence getSummary() {
        return ((BaseBluetoothDialogPreference) this.mPreference).generateSummary(getCurrentConfigIndex());
    }

    public void onIndexUpdated(int i) {
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        if (bluetoothA2dp != null) {
            writeConfigurationValues(i);
            BluetoothCodecConfig createCodecConfig = this.mBluetoothA2dpConfigStore.createCodecConfig();
            BluetoothDevice a2dpActiveDevice = getA2dpActiveDevice();
            if (a2dpActiveDevice != null) {
                bluetoothA2dp.setCodecConfigPreference(a2dpActiveDevice, createCodecConfig);
            }
            Preference preference = this.mPreference;
            preference.setSummary((CharSequence) ((BaseBluetoothDialogPreference) preference).generateSummary(i));
        }
    }

    public int getCurrentConfigIndex() {
        BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
        if (currentCodecConfig != null) {
            return getCurrentIndexByConfig(currentCodecConfig);
        }
        Log.d("AbstractBtDlgCtr", "Unable to get current config index. Current codec Config is null.");
        return getDefaultIndex();
    }

    public void onBluetoothServiceConnected(BluetoothA2dp bluetoothA2dp) {
        super.onBluetoothServiceConnected(bluetoothA2dp);
        initConfigStore();
    }

    private void initConfigStore() {
        BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
        if (currentCodecConfig != null) {
            this.mBluetoothA2dpConfigStore.setCodecType(currentCodecConfig.getCodecType());
            this.mBluetoothA2dpConfigStore.setSampleRate(currentCodecConfig.getSampleRate());
            this.mBluetoothA2dpConfigStore.setBitsPerSample(currentCodecConfig.getBitsPerSample());
            this.mBluetoothA2dpConfigStore.setChannelMode(currentCodecConfig.getChannelMode());
            this.mBluetoothA2dpConfigStore.setCodecPriority(1000000);
            this.mBluetoothA2dpConfigStore.setCodecSpecific1Value(currentCodecConfig.getCodecSpecific1());
        }
    }

    /* access modifiers changed from: protected */
    public int getDefaultIndex() {
        return ((BaseBluetoothDialogPreference) this.mPreference).getDefaultIndex();
    }

    /* access modifiers changed from: protected */
    public BluetoothCodecConfig getCurrentCodecConfig() {
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        if (bluetoothA2dp == null) {
            return null;
        }
        BluetoothDevice a2dpActiveDevice = getA2dpActiveDevice();
        if (a2dpActiveDevice == null) {
            Log.d("AbstractBtDlgCtr", "Unable to get current codec config. No active device.");
            return null;
        }
        BluetoothCodecStatus codecStatus = bluetoothA2dp.getCodecStatus(a2dpActiveDevice);
        if (codecStatus != null) {
            return codecStatus.getCodecConfig();
        }
        Log.d("AbstractBtDlgCtr", "Unable to get current codec config. Codec status is null");
        return null;
    }

    /* access modifiers changed from: protected */
    public List<BluetoothCodecConfig> getSelectableConfigs(BluetoothDevice bluetoothDevice) {
        BluetoothCodecStatus codecStatus;
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        if (bluetoothA2dp == null) {
            return null;
        }
        if (bluetoothDevice == null) {
            bluetoothDevice = getA2dpActiveDevice();
        }
        if (bluetoothDevice == null || (codecStatus = bluetoothA2dp.getCodecStatus(bluetoothDevice)) == null) {
            return null;
        }
        return codecStatus.getCodecsSelectableCapabilities();
    }

    /* access modifiers changed from: protected */
    public BluetoothCodecConfig getSelectableByCodecType(int i) {
        BluetoothDevice a2dpActiveDevice = getA2dpActiveDevice();
        if (a2dpActiveDevice == null) {
            Log.d("AbstractBtDlgCtr", "Unable to get selectable config. No active device.");
            return null;
        }
        for (BluetoothCodecConfig next : getSelectableConfigs(a2dpActiveDevice)) {
            if (next.getCodecType() == i) {
                return next;
            }
        }
        Log.d("AbstractBtDlgCtr", "Unable to find matching codec config, type is " + i);
        return null;
    }

    static int getHighestCodec(BluetoothA2dp bluetoothA2dp, BluetoothDevice bluetoothDevice, List<BluetoothCodecConfig> list) {
        if (list == null) {
            Log.d("AbstractBtDlgCtr", "Unable to get highest codec. Configs are empty");
            return 1000000;
        }
        int isOptionalCodecsEnabled = bluetoothA2dp.isOptionalCodecsEnabled(bluetoothDevice);
        if (isOptionalCodecsEnabled != 1) {
            return 0;
        }
        for (int i = 0; i < CODEC_TYPES.length; i++) {
            for (BluetoothCodecConfig codecType : list) {
                int codecType2 = codecType.getCodecType();
                int[] iArr = CODEC_TYPES;
                if (codecType2 == iArr[i]) {
                    return iArr[i];
                }
            }
        }
        return 1000000;
    }

    static int getHighestSampleRate(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.d("AbstractBtDlgCtr", "Unable to get highest sample rate. Config is empty");
            return 0;
        }
        int sampleRate = bluetoothCodecConfig.getSampleRate();
        int i = 0;
        while (true) {
            int[] iArr = SAMPLE_RATES;
            if (i >= iArr.length) {
                return 0;
            }
            if ((iArr[i] & sampleRate) != 0) {
                return iArr[i];
            }
            i++;
        }
    }

    static int getHighestBitsPerSample(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.d("AbstractBtDlgCtr", "Unable to get highest bits per sample. Config is empty");
            return 0;
        }
        int bitsPerSample = bluetoothCodecConfig.getBitsPerSample();
        int i = 0;
        while (true) {
            int[] iArr = BITS_PER_SAMPLES;
            if (i >= iArr.length) {
                return 0;
            }
            if ((iArr[i] & bitsPerSample) != 0) {
                return iArr[i];
            }
            i++;
        }
    }

    static int getHighestChannelMode(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.d("AbstractBtDlgCtr", "Unable to get highest channel mode. Config is empty");
            return 0;
        }
        int channelMode = bluetoothCodecConfig.getChannelMode();
        int i = 0;
        while (true) {
            int[] iArr = CHANNEL_MODES;
            if (i >= iArr.length) {
                return 0;
            }
            if ((iArr[i] & channelMode) != 0) {
                return iArr[i];
            }
            i++;
        }
    }
}
