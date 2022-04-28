package com.android.settings.connecteddevice.usb;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.net.TetheringManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.util.Log;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.settings.Utils;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.LinkedHashMap;
import java.util.Map;

public class UsbDetailsFunctionsController extends UsbDetailsController implements SelectorWithWidgetPreference.OnClickListener {
    private static final boolean DEBUG = Log.isLoggable("UsbFunctionsCtrl", 3);
    static final Map<Long, Integer> FUNCTIONS_MAP;
    private Handler mHandler;
    OnStartTetheringCallback mOnStartTetheringCallback = new OnStartTetheringCallback();
    long mPreviousFunction = this.mUsbBackend.getCurrentFunctions();
    private PreferenceCategory mProfilesContainer;
    private TetheringManager mTetheringManager;

    private boolean isAccessoryMode(long j) {
        return (j & 2) != 0;
    }

    public String getPreferenceKey() {
        return "usb_details_functions";
    }

    static {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        FUNCTIONS_MAP = linkedHashMap;
        linkedHashMap.put(4L, Integer.valueOf(C0444R.string.usb_use_file_transfers));
        linkedHashMap.put(32L, Integer.valueOf(C0444R.string.usb_use_tethering));
        linkedHashMap.put(8L, Integer.valueOf(C0444R.string.usb_use_MIDI));
        linkedHashMap.put(16L, Integer.valueOf(C0444R.string.usb_use_photo_transfers));
        linkedHashMap.put(0L, Integer.valueOf(C0444R.string.usb_use_charging_only));
    }

    public UsbDetailsFunctionsController(Context context, UsbDetailsFragment usbDetailsFragment, UsbBackend usbBackend) {
        super(context, usbDetailsFragment, usbBackend);
        this.mTetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
        this.mHandler = new Handler(context.getMainLooper());
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mProfilesContainer = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        refresh(false, this.mUsbBackend.getDefaultUsbFunctions(), 0, 0);
    }

    private SelectorWithWidgetPreference getProfilePreference(String str, int i) {
        SelectorWithWidgetPreference selectorWithWidgetPreference = (SelectorWithWidgetPreference) this.mProfilesContainer.findPreference(str);
        if (selectorWithWidgetPreference != null) {
            return selectorWithWidgetPreference;
        }
        SelectorWithWidgetPreference selectorWithWidgetPreference2 = new SelectorWithWidgetPreference(this.mProfilesContainer.getContext());
        selectorWithWidgetPreference2.setKey(str);
        selectorWithWidgetPreference2.setTitle(i);
        selectorWithWidgetPreference2.setSingleLineTitle(false);
        selectorWithWidgetPreference2.setOnClickListener(this);
        this.mProfilesContainer.addPreference(selectorWithWidgetPreference2);
        return selectorWithWidgetPreference2;
    }

    /* access modifiers changed from: protected */
    public void refresh(boolean z, long j, int i, int i2) {
        if (DEBUG) {
            Log.d("UsbFunctionsCtrl", "refresh() connected : " + z + ", functions : " + j + ", powerRole : " + i + ", dataRole : " + i2);
        }
        if (!z || i2 != 2) {
            this.mProfilesContainer.setEnabled(false);
        } else {
            this.mProfilesContainer.setEnabled(true);
        }
        for (Long longValue : FUNCTIONS_MAP.keySet()) {
            long longValue2 = longValue.longValue();
            SelectorWithWidgetPreference profilePreference = getProfilePreference(UsbBackend.usbFunctionsToString(longValue2), FUNCTIONS_MAP.get(Long.valueOf(longValue2)).intValue());
            if (!this.mUsbBackend.areFunctionsSupported(longValue2)) {
                this.mProfilesContainer.removePreference(profilePreference);
            } else if (isAccessoryMode(j)) {
                profilePreference.setChecked(4 == longValue2);
            } else if (j == 1024) {
                profilePreference.setChecked(32 == longValue2);
            } else {
                profilePreference.setChecked(j == longValue2);
            }
        }
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        long usbFunctionsFromString = UsbBackend.usbFunctionsFromString(selectorWithWidgetPreference.getKey());
        long currentFunctions = this.mUsbBackend.getCurrentFunctions();
        if (DEBUG) {
            Log.d("UsbFunctionsCtrl", "onRadioButtonClicked() function : " + usbFunctionsFromString + ", toString() : " + UsbManager.usbFunctionsToString(usbFunctionsFromString) + ", previousFunction : " + currentFunctions + ", toString() : " + UsbManager.usbFunctionsToString(currentFunctions));
        }
        if (usbFunctionsFromString != currentFunctions && !Utils.isMonkeyRunning() && !isClickEventIgnored(usbFunctionsFromString, currentFunctions)) {
            this.mPreviousFunction = currentFunctions;
            SelectorWithWidgetPreference selectorWithWidgetPreference2 = (SelectorWithWidgetPreference) this.mProfilesContainer.findPreference(UsbBackend.usbFunctionsToString(currentFunctions));
            if (selectorWithWidgetPreference2 != null) {
                selectorWithWidgetPreference2.setChecked(false);
                selectorWithWidgetPreference.setChecked(true);
            }
            if (usbFunctionsFromString == 32 || usbFunctionsFromString == 1024) {
                this.mTetheringManager.startTethering(1, new HandlerExecutor(this.mHandler), this.mOnStartTetheringCallback);
            } else {
                this.mUsbBackend.setCurrentFunctions(usbFunctionsFromString);
            }
        }
    }

    private boolean isClickEventIgnored(long j, long j2) {
        return isAccessoryMode(j2) && j == 4;
    }

    public boolean isAvailable() {
        return !Utils.isMonkeyRunning();
    }

    final class OnStartTetheringCallback implements TetheringManager.StartTetheringCallback {
        OnStartTetheringCallback() {
        }

        public void onTetheringFailed(int i) {
            Log.w("UsbFunctionsCtrl", "onTetheringFailed() error : " + i);
            UsbDetailsFunctionsController usbDetailsFunctionsController = UsbDetailsFunctionsController.this;
            usbDetailsFunctionsController.mUsbBackend.setCurrentFunctions(usbDetailsFunctionsController.mPreviousFunction);
        }
    }
}
