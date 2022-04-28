package com.android.settings.connecteddevice.usb;

import android.content.Context;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class UsbDetailsDataRoleController extends UsbDetailsController implements SelectorWithWidgetPreference.OnClickListener {
    private SelectorWithWidgetPreference mDevicePref;
    private final Runnable mFailureCallback = new UsbDetailsDataRoleController$$ExternalSyntheticLambda0(this);
    private SelectorWithWidgetPreference mHostPref;
    private SelectorWithWidgetPreference mNextRolePref;
    private PreferenceCategory mPreferenceCategory;

    public String getPreferenceKey() {
        return "usb_details_data_role";
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        SelectorWithWidgetPreference selectorWithWidgetPreference = this.mNextRolePref;
        if (selectorWithWidgetPreference != null) {
            selectorWithWidgetPreference.setSummary((int) C0444R.string.usb_switching_failed);
            this.mNextRolePref = null;
        }
    }

    public UsbDetailsDataRoleController(Context context, UsbDetailsFragment usbDetailsFragment, UsbBackend usbBackend) {
        super(context, usbDetailsFragment, usbBackend);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mHostPref = makeRadioPreference(UsbBackend.dataRoleToString(1), C0444R.string.usb_control_host);
        this.mDevicePref = makeRadioPreference(UsbBackend.dataRoleToString(2), C0444R.string.usb_control_device);
    }

    /* access modifiers changed from: protected */
    public void refresh(boolean z, long j, int i, int i2) {
        if (i2 == 2) {
            this.mDevicePref.setChecked(true);
            this.mHostPref.setChecked(false);
            this.mPreferenceCategory.setEnabled(true);
        } else if (i2 == 1) {
            this.mDevicePref.setChecked(false);
            this.mHostPref.setChecked(true);
            this.mPreferenceCategory.setEnabled(true);
        } else if (!z || i2 == 0) {
            this.mPreferenceCategory.setEnabled(false);
            if (this.mNextRolePref == null) {
                this.mHostPref.setSummary((CharSequence) "");
                this.mDevicePref.setSummary((CharSequence) "");
            }
        }
        SelectorWithWidgetPreference selectorWithWidgetPreference = this.mNextRolePref;
        if (selectorWithWidgetPreference != null && i2 != 0) {
            if (UsbBackend.dataRoleFromString(selectorWithWidgetPreference.getKey()) == i2) {
                this.mNextRolePref.setSummary((CharSequence) "");
            } else {
                this.mNextRolePref.setSummary((int) C0444R.string.usb_switching_failed);
            }
            this.mNextRolePref = null;
            this.mHandler.removeCallbacks(this.mFailureCallback);
        }
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        int dataRoleFromString = UsbBackend.dataRoleFromString(selectorWithWidgetPreference.getKey());
        if (dataRoleFromString != this.mUsbBackend.getDataRole() && this.mNextRolePref == null && !Utils.isMonkeyRunning()) {
            this.mUsbBackend.setDataRole(dataRoleFromString);
            this.mNextRolePref = selectorWithWidgetPreference;
            selectorWithWidgetPreference.setSummary((int) C0444R.string.usb_switching);
            this.mHandler.postDelayed(this.mFailureCallback, this.mUsbBackend.areAllRolesSupported() ? 4000 : 15000);
        }
    }

    public boolean isAvailable() {
        return !Utils.isMonkeyRunning();
    }

    private SelectorWithWidgetPreference makeRadioPreference(String str, int i) {
        SelectorWithWidgetPreference selectorWithWidgetPreference = new SelectorWithWidgetPreference(this.mPreferenceCategory.getContext());
        selectorWithWidgetPreference.setKey(str);
        selectorWithWidgetPreference.setTitle(i);
        selectorWithWidgetPreference.setOnClickListener(this);
        this.mPreferenceCategory.addPreference(selectorWithWidgetPreference);
        return selectorWithWidgetPreference;
    }
}
