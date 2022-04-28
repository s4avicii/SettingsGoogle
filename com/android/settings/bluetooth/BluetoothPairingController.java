package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.widget.CompoundButton;
import androidx.window.C0444R;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import java.util.Locale;

public class BluetoothPairingController implements CompoundButton.OnCheckedChangeListener {
    private LocalBluetoothManager mBluetoothManager;
    private BluetoothDevice mDevice;
    private String mDeviceName;
    private int mInitiator;
    private boolean mIsCoordinatedSetMember;
    private int mPasskey;
    private String mPasskeyFormatted;
    private boolean mPbapAllowed;
    private LocalBluetoothProfile mPbapClientProfile;
    int mType;
    private String mUserInput;

    public BluetoothPairingController(Intent intent, Context context) {
        boolean z;
        this.mBluetoothManager = Utils.getLocalBtManager(context);
        BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        this.mDevice = bluetoothDevice;
        if (this.mBluetoothManager == null) {
            throw new IllegalStateException("Could not obtain LocalBluetoothManager");
        } else if (bluetoothDevice != null) {
            this.mType = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_VARIANT", Integer.MIN_VALUE);
            this.mPasskey = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", Integer.MIN_VALUE);
            this.mInitiator = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_INITIATOR", Integer.MIN_VALUE);
            this.mDeviceName = this.mBluetoothManager.getCachedDeviceManager().getName(this.mDevice);
            this.mPbapClientProfile = this.mBluetoothManager.getProfileManager().getPbapClientProfile();
            this.mPasskeyFormatted = formatKey(this.mPasskey);
            CachedBluetoothDevice findDevice = this.mBluetoothManager.getCachedDeviceManager().findDevice(this.mDevice);
            if (findDevice != null) {
                z = findDevice.isCoordinatedSetMemberDevice();
            } else {
                z = false;
            }
            this.mIsCoordinatedSetMember = z;
        } else {
            throw new IllegalStateException("Could not find BluetoothDevice");
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            this.mPbapAllowed = true;
        } else {
            this.mPbapAllowed = false;
        }
    }

    public void onDialogPositiveClick(BluetoothPairingDialogFragment bluetoothPairingDialogFragment) {
        if (this.mPbapAllowed) {
            this.mDevice.setPhonebookAccessPermission(1);
        } else {
            this.mDevice.setPhonebookAccessPermission(2);
        }
        if (getDialogType() == 0) {
            onPair(this.mUserInput);
        } else {
            onPair((String) null);
        }
    }

    public void onDialogNegativeClick(BluetoothPairingDialogFragment bluetoothPairingDialogFragment) {
        this.mDevice.setPhonebookAccessPermission(2);
        onCancel();
    }

    public int getDialogType() {
        switch (this.mType) {
            case 0:
            case 1:
            case 7:
                return 0;
            case 2:
            case 3:
            case 6:
                return 1;
            case 4:
            case 5:
                return 2;
            default:
                return -1;
        }
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public boolean isCoordinatedSetMemberDevice() {
        return this.mIsCoordinatedSetMember;
    }

    public boolean isProfileReady() {
        LocalBluetoothProfile localBluetoothProfile = this.mPbapClientProfile;
        return localBluetoothProfile != null && localBluetoothProfile.isProfileReady();
    }

    public boolean getContactSharingState() {
        int phonebookAccessPermission = this.mDevice.getPhonebookAccessPermission();
        if (phonebookAccessPermission == 1) {
            return true;
        }
        if (phonebookAccessPermission == 2 || this.mDevice.getBluetoothClass().getDeviceClass() != 1032) {
            return false;
        }
        if (1 == this.mInitiator) {
            return true;
        }
        return false;
    }

    public void setContactSharingState() {
        int phonebookAccessPermission = this.mDevice.getPhonebookAccessPermission();
        if (phonebookAccessPermission == 1 || (phonebookAccessPermission == 0 && this.mDevice.getBluetoothClass().getDeviceClass() == 1032)) {
            onCheckedChanged((CompoundButton) null, true);
        } else {
            onCheckedChanged((CompoundButton) null, false);
        }
    }

    public boolean isPasskeyValid(Editable editable) {
        boolean z = this.mType == 7;
        if (editable.length() >= 16 && z) {
            return true;
        }
        if (editable.length() <= 0 || z) {
            return false;
        }
        return true;
    }

    public int getDeviceVariantMessageId() {
        int i = this.mType;
        if (i == 0) {
            return C0444R.string.bluetooth_enter_pin_other_device;
        }
        if (i == 1) {
            return C0444R.string.bluetooth_enter_passkey_other_device;
        }
        if (i != 7) {
            return -1;
        }
        return C0444R.string.bluetooth_enter_pin_other_device;
    }

    public int getDeviceVariantMessageHintId() {
        int i = this.mType;
        if (i == 0 || i == 1) {
            return C0444R.string.bluetooth_pin_values_hint;
        }
        if (i != 7) {
            return -1;
        }
        return C0444R.string.bluetooth_pin_values_hint_16_digits;
    }

    public int getDeviceMaxPasskeyLength() {
        int i = this.mType;
        if (i == 0) {
            return 16;
        }
        if (i != 1) {
            return i != 7 ? 0 : 16;
        }
        return 6;
    }

    public boolean pairingCodeIsAlphanumeric() {
        return this.mType != 1;
    }

    /* access modifiers changed from: protected */
    public void notifyDialogDisplayed() {
        int i = this.mType;
        if (i == 4) {
            this.mDevice.setPairingConfirmation(true);
        } else if (i == 5) {
            this.mDevice.setPin(this.mPasskeyFormatted);
        }
    }

    public boolean isDisplayPairingKeyVariant() {
        int i = this.mType;
        return i == 4 || i == 5 || i == 6;
    }

    public boolean hasPairingContent() {
        int i = this.mType;
        return i == 2 || i == 4 || i == 5;
    }

    public String getPairingContent() {
        if (hasPairingContent()) {
            return this.mPasskeyFormatted;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void updateUserInput(String str) {
        this.mUserInput = str;
    }

    private String formatKey(int i) {
        int i2 = this.mType;
        if (i2 == 2 || i2 == 4) {
            return String.format(Locale.US, "%06d", new Object[]{Integer.valueOf(i)});
        } else if (i2 != 5) {
            return null;
        } else {
            return String.format("%04d", new Object[]{Integer.valueOf(i)});
        }
    }

    private void onPair(String str) {
        Log.d("BTPairingController", "Pairing dialog accepted");
        switch (this.mType) {
            case 0:
            case 7:
                this.mDevice.setPin(str);
                return;
            case 1:
            case 4:
            case 5:
            case 6:
                return;
            case 2:
            case 3:
                this.mDevice.setPairingConfirmation(true);
                return;
            default:
                Log.e("BTPairingController", "Incorrect pairing type received");
                return;
        }
    }

    public void onCancel() {
        Log.d("BTPairingController", "Pairing dialog canceled");
        this.mDevice.cancelBondProcess();
    }

    public boolean deviceEquals(BluetoothDevice bluetoothDevice) {
        return this.mDevice == bluetoothDevice;
    }
}
