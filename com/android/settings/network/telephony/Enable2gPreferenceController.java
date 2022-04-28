package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.network.CarrierConfigCache;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class Enable2gPreferenceController extends TelephonyTogglePreferenceController {
    private static final long BITMASK_2G = 32843;
    private static final String LOG_TAG = "Enable2gPreferenceController";
    private CarrierConfigCache mCarrierConfigCache;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private SubscriptionManager mSubscriptionManager;
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

    public Enable2gPreferenceController(Context context, String str) {
        super(context, str);
        this.mCarrierConfigCache = CarrierConfigCache.getInstance(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
    }

    public Enable2gPreferenceController init(int i) {
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        return this;
    }

    public void updateState(Preference preference) {
        String str;
        super.updateState(preference);
        if (preference != null && SubscriptionManager.isUsableSubscriptionId(this.mSubId)) {
            PersistableBundle configForSubId = this.mCarrierConfigCache.getConfigForSubId(this.mSubId);
            boolean z = configForSubId != null && configForSubId.getBoolean("hide_enable_2g_bool");
            preference.setEnabled(!z);
            if (z) {
                str = this.mContext.getString(C0444R.string.enable_2g_summary_disabled_carrier, new Object[]{getCarrierName()});
            } else {
                str = this.mContext.getString(C0444R.string.enable_2g_summary);
            }
            preference.setSummary((CharSequence) str);
        }
    }

    private String getCarrierName() {
        SubscriptionInfo subById = SubscriptionUtil.getSubById(this.mSubscriptionManager, this.mSubId);
        if (subById == null) {
            return "";
        }
        return subById.getCarrierName().toString();
    }

    public int getAvailabilityStatus(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigCache.getConfigForSubId(i);
        if (this.mTelephonyManager == null) {
            Log.w(LOG_TAG, "Telephony manager not yet initialized");
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        }
        if (SubscriptionManager.isUsableSubscriptionId(i) && configForSubId != null && this.mTelephonyManager.isRadioInterfaceCapabilitySupported("CAPABILITY_USES_ALLOWED_NETWORK_TYPES_BITMASK")) {
            return 0;
        }
        return 2;
    }

    public boolean isChecked() {
        return (this.mTelephonyManager.getAllowedNetworkTypesForReason(3) & BITMASK_2G) != 0;
    }

    public boolean setChecked(boolean z) {
        long j;
        if (!SubscriptionManager.isUsableSubscriptionId(this.mSubId)) {
            return false;
        }
        long allowedNetworkTypesForReason = this.mTelephonyManager.getAllowedNetworkTypesForReason(3);
        if (((allowedNetworkTypesForReason & BITMASK_2G) != 0) == z) {
            return false;
        }
        if (z) {
            j = allowedNetworkTypesForReason | BITMASK_2G;
            Log.i(LOG_TAG, "Enabling 2g. Allowed network types: " + j);
        } else {
            j = allowedNetworkTypesForReason & -32844;
            Log.i(LOG_TAG, "Disabling 2g. Allowed network types: " + j);
        }
        this.mTelephonyManager.setAllowedNetworkTypesForReason(3, j);
        this.mMetricsFeatureProvider.action(this.mContext, 1761, z);
        return true;
    }
}
