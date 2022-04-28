package com.android.settings.bluetooth;

import android.content.Context;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.FooterPreference;

public class BluetoothDetailsMacAddressController extends BluetoothDetailsController {
    private FooterPreference mFooterPreference;

    public String getPreferenceKey() {
        return "device_details_footer";
    }

    public BluetoothDetailsMacAddressController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        FooterPreference footerPreference = (FooterPreference) preferenceScreen.findPreference("device_details_footer");
        this.mFooterPreference = footerPreference;
        footerPreference.setTitle((CharSequence) this.mContext.getString(C0444R.string.bluetooth_device_mac_address, new Object[]{this.mCachedDevice.getIdentityAddress()}));
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        if (this.mCachedDevice.getGroupId() != -1) {
            StringBuilder sb = new StringBuilder(this.mContext.getString(C0444R.string.bluetooth_multuple_devices_mac_address, new Object[]{this.mCachedDevice.getIdentityAddress()}));
            for (CachedBluetoothDevice identityAddress : this.mCachedDevice.getMemberDevice()) {
                sb.append("\n");
                sb.append(identityAddress.getIdentityAddress());
            }
            this.mFooterPreference.setTitle((CharSequence) sb);
            return;
        }
        this.mFooterPreference.setTitle((CharSequence) this.mContext.getString(C0444R.string.bluetooth_device_mac_address, new Object[]{this.mCachedDevice.getIdentityAddress()}));
    }
}
