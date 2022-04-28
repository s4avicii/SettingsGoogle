package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class NrAdvancedCallingPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String TAG = "VoNrSettings";
    /* access modifiers changed from: private */
    public Integer mCallState;
    private boolean mHas5gCapability = false;
    private boolean mIsNrEnableFromCarrierConfig = false;
    private boolean mIsVonrEnabledFromCarrierConfig = false;
    private boolean mIsVonrVisibleFromCarrierConfig = false;
    Preference mPreference;
    /* access modifiers changed from: private */
    public PhoneCallStateTelephonyCallback mTelephonyCallback;
    private TelephonyManager mTelephonyManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NrAdvancedCallingPreferenceController(Context context, String str) {
        super(context, str);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
    }

    public NrAdvancedCallingPreferenceController init(int i) {
        Log.d(TAG, "init: ");
        if (this.mTelephonyCallback == null) {
            this.mTelephonyCallback = new PhoneCallStateTelephonyCallback();
        }
        this.mSubId = i;
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        }
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
        }
        this.mHas5gCapability = (this.mTelephonyManager.getSupportedRadioAccessFamily() & 524288) > 0;
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
        if (carrierConfigForSubId == null) {
            return this;
        }
        this.mIsVonrEnabledFromCarrierConfig = carrierConfigForSubId.getBoolean("vonr_enabled_bool");
        this.mIsVonrVisibleFromCarrierConfig = carrierConfigForSubId.getBoolean("vonr_setting_visibility_bool");
        this.mIsNrEnableFromCarrierConfig = !ArrayUtils.isEmpty(carrierConfigForSubId.getIntArray("carrier_nr_availabilities_int_array"));
        Log.d(TAG, "mHas5gCapability: " + this.mHas5gCapability + ",mIsNrEnabledFromCarrierConfig: " + this.mIsNrEnableFromCarrierConfig + ",mIsVonrEnabledFromCarrierConfig: " + this.mIsVonrEnabledFromCarrierConfig + ",mIsVonrVisibleFromCarrierConfig: " + this.mIsVonrVisibleFromCarrierConfig);
        return this;
    }

    public int getAvailabilityStatus(int i) {
        init(i);
        return (!this.mHas5gCapability || !this.mIsNrEnableFromCarrierConfig || !this.mIsVonrEnabledFromCarrierConfig || !this.mIsVonrVisibleFromCarrierConfig) ? 2 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        PhoneCallStateTelephonyCallback phoneCallStateTelephonyCallback = this.mTelephonyCallback;
        if (phoneCallStateTelephonyCallback != null) {
            phoneCallStateTelephonyCallback.register(this.mTelephonyManager);
        }
    }

    public void onStop() {
        PhoneCallStateTelephonyCallback phoneCallStateTelephonyCallback = this.mTelephonyCallback;
        if (phoneCallStateTelephonyCallback != null) {
            phoneCallStateTelephonyCallback.unregister();
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            ((SwitchPreference) preference).setEnabled(isUserControlAllowed());
        }
    }

    public boolean setChecked(boolean z) {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return false;
        }
        Log.d(TAG, "setChecked: " + z);
        int voNrEnabled = this.mTelephonyManager.setVoNrEnabled(z);
        if (voNrEnabled == 0) {
            return true;
        }
        Log.d(TAG, "Fail to set VoNR result= " + voNrEnabled + ". subId=" + this.mSubId);
        return false;
    }

    public boolean isChecked() {
        return this.mTelephonyManager.isVoNrEnabled();
    }

    /* access modifiers changed from: protected */
    public boolean isCallStateIdle() {
        Integer num = this.mCallState;
        return num != null && num.intValue() == 0;
    }

    private boolean isUserControlAllowed() {
        return isCallStateIdle();
    }

    private class PhoneCallStateTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private TelephonyManager mLocalTelephonyManager;

        private PhoneCallStateTelephonyCallback() {
        }

        public void onCallStateChanged(int i) {
            NrAdvancedCallingPreferenceController.this.mCallState = Integer.valueOf(i);
            NrAdvancedCallingPreferenceController nrAdvancedCallingPreferenceController = NrAdvancedCallingPreferenceController.this;
            nrAdvancedCallingPreferenceController.updateState(nrAdvancedCallingPreferenceController.mPreference);
        }

        public void register(TelephonyManager telephonyManager) {
            this.mLocalTelephonyManager = telephonyManager;
            NrAdvancedCallingPreferenceController.this.mCallState = Integer.valueOf(telephonyManager.getCallState());
            this.mLocalTelephonyManager.registerTelephonyCallback(NrAdvancedCallingPreferenceController.this.mContext.getMainExecutor(), NrAdvancedCallingPreferenceController.this.mTelephonyCallback);
        }

        public void unregister() {
            NrAdvancedCallingPreferenceController.this.mCallState = null;
            TelephonyManager telephonyManager = this.mLocalTelephonyManager;
            if (telephonyManager != null) {
                telephonyManager.unregisterTelephonyCallback(this);
            }
        }
    }
}
