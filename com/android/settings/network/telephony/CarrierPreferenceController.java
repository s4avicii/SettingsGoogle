package com.android.settings.network.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import androidx.preference.Preference;
import com.android.settings.network.CarrierConfigCache;

public class CarrierPreferenceController extends TelephonyBasePreferenceController {
    CarrierConfigCache mCarrierConfigCache;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public CarrierPreferenceController(Context context, String str) {
        super(context, str);
        this.mCarrierConfigCache = CarrierConfigCache.getInstance(context);
    }

    public void init(int i) {
        this.mSubId = i;
    }

    public int getAvailabilityStatus(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigCache.getConfigForSubId(i);
        return (configForSubId == null || !configForSubId.getBoolean("carrier_settings_enable_bool") || (!MobileNetworkUtils.isCdmaOptions(this.mContext, i) && !MobileNetworkUtils.isGsmOptions(this.mContext, i))) ? 2 : 0;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!getPreferenceKey().equals(preference.getKey())) {
            return false;
        }
        Intent carrierSettingsActivityIntent = getCarrierSettingsActivityIntent(this.mSubId);
        if (carrierSettingsActivityIntent == null) {
            return true;
        }
        this.mContext.startActivity(carrierSettingsActivityIntent);
        return true;
    }

    private Intent getCarrierSettingsActivityIntent(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigCache.getConfigForSubId(i);
        String str = "";
        if (configForSubId != null) {
            str = configForSubId.getString("carrier_settings_activity_component_name_string", str);
        }
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        if (unflattenFromString == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(unflattenFromString);
        intent.setFlags(268435456);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", i);
        if (this.mContext.getPackageManager().resolveActivity(intent, 0) != null) {
            return intent;
        }
        return null;
    }
}
