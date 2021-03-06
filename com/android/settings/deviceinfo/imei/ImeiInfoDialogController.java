package com.android.settings.deviceinfo.imei;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.window.C0444R;

public class ImeiInfoDialogController {
    static final int ID_CDMA_SETTINGS = 2131558723;
    static final int ID_GSM_SETTINGS = 2131559026;
    static final int ID_IMEI_SV_VALUE = 2131559140;
    static final int ID_IMEI_VALUE = 2131559141;
    static final int ID_MEID_NUMBER_VALUE = 2131559292;
    static final int ID_MIN_NUMBER_VALUE = 2131559307;
    static final int ID_PRL_VERSION_VALUE = 2131559552;
    private final ImeiInfoDialogFragment mDialog;
    private final int mSlotId;
    private final SubscriptionInfo mSubscriptionInfo;
    private final TelephonyManager mTelephonyManager;

    public ImeiInfoDialogController(ImeiInfoDialogFragment imeiInfoDialogFragment, int i) {
        this.mDialog = imeiInfoDialogFragment;
        this.mSlotId = i;
        Context context = imeiInfoDialogFragment.getContext();
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = ((SubscriptionManager) context.getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfoForSimSlotIndex(i);
        this.mSubscriptionInfo = activeSubscriptionInfoForSimSlotIndex;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        if (activeSubscriptionInfoForSimSlotIndex != null) {
            this.mTelephonyManager = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(activeSubscriptionInfoForSimSlotIndex.getSubscriptionId());
        } else if (isValidSlotIndex(i, telephonyManager)) {
            this.mTelephonyManager = telephonyManager;
        } else {
            this.mTelephonyManager = null;
        }
    }

    public void populateImeiInfo() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            Log.w("ImeiInfoDialog", "TelephonyManager for this slot is null. Invalid slot? id=" + this.mSlotId);
        } else if (telephonyManager.getPhoneType() == 2) {
            updateDialogForCdmaPhone();
        } else {
            updateDialogForGsmPhone();
        }
    }

    private void updateDialogForCdmaPhone() {
        Resources resources = this.mDialog.getContext().getResources();
        this.mDialog.setText(C0444R.C0448id.meid_number_value, getMeid());
        ImeiInfoDialogFragment imeiInfoDialogFragment = this.mDialog;
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        imeiInfoDialogFragment.setText(C0444R.C0448id.min_number_value, subscriptionInfo != null ? this.mTelephonyManager.getCdmaMin(subscriptionInfo.getSubscriptionId()) : "");
        if (resources.getBoolean(C0444R.bool.config_msid_enable)) {
            this.mDialog.setText(C0444R.C0448id.min_number_label, resources.getString(C0444R.string.status_msid_number));
        }
        this.mDialog.setText(C0444R.C0448id.prl_version_value, getCdmaPrlVersion());
        if ((this.mSubscriptionInfo == null || !isCdmaLteEnabled()) && (this.mSubscriptionInfo != null || !isSimPresent(this.mSlotId))) {
            this.mDialog.removeViewFromScreen(C0444R.C0448id.gsm_settings);
            return;
        }
        this.mDialog.setText(C0444R.C0448id.imei_value, this.mTelephonyManager.getImei(this.mSlotId));
        this.mDialog.setText(C0444R.C0448id.imei_sv_value, this.mTelephonyManager.getDeviceSoftwareVersion(this.mSlotId));
    }

    private void updateDialogForGsmPhone() {
        this.mDialog.setText(C0444R.C0448id.imei_value, this.mTelephonyManager.getImei(this.mSlotId));
        this.mDialog.setText(C0444R.C0448id.imei_sv_value, this.mTelephonyManager.getDeviceSoftwareVersion(this.mSlotId));
        this.mDialog.removeViewFromScreen(C0444R.C0448id.cdma_settings);
    }

    /* access modifiers changed from: package-private */
    public String getCdmaPrlVersion() {
        return this.mSubscriptionInfo != null ? this.mTelephonyManager.getCdmaPrlVersion() : "";
    }

    /* access modifiers changed from: package-private */
    public boolean isCdmaLteEnabled() {
        return this.mTelephonyManager.isLteCdmaEvdoGsmWcdmaEnabled();
    }

    /* access modifiers changed from: package-private */
    public boolean isSimPresent(int i) {
        int simState = this.mTelephonyManager.getSimState(i);
        return (simState == 1 || simState == 0) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public String getMeid() {
        return this.mTelephonyManager.getMeid(this.mSlotId);
    }

    private boolean isValidSlotIndex(int i, TelephonyManager telephonyManager) {
        return i >= 0 && i < telephonyManager.getPhoneCount();
    }
}
