package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.overlay.FeatureFactory;
import java.util.Objects;

class PrivacyPreferenceControllerHelper {
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final EnterprisePrivacyFeatureProvider mFeatureProvider;

    PrivacyPreferenceControllerHelper(Context context) {
        Objects.requireNonNull(context);
        Context context2 = context;
        this.mContext = context;
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    /* access modifiers changed from: package-private */
    public void updateState(Preference preference) {
        if (preference != null) {
            String deviceOwnerOrganizationName = this.mFeatureProvider.getDeviceOwnerOrganizationName();
            if (deviceOwnerOrganizationName == null) {
                preference.setSummary((CharSequence) this.mDevicePolicyManager.getString("Settings.MANAGED_DEVICE_INFO_SUMMARY", new PrivacyPreferenceControllerHelper$$ExternalSyntheticLambda0(this)));
                return;
            }
            preference.setSummary((CharSequence) this.mDevicePolicyManager.getString("Settings.MANAGED_DEVICE_INFO_SUMMARY_WITH_NAME", new PrivacyPreferenceControllerHelper$$ExternalSyntheticLambda1(this, deviceOwnerOrganizationName), new Object[]{deviceOwnerOrganizationName}));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateState$0() throws Exception {
        return this.mContext.getString(C0444R.string.enterprise_privacy_settings_summary_generic);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateState$1(String str) throws Exception {
        return this.mContext.getResources().getString(C0444R.string.enterprise_privacy_settings_summary_with_name, new Object[]{str});
    }

    /* access modifiers changed from: package-private */
    public boolean hasDeviceOwner() {
        return this.mFeatureProvider.hasDeviceOwner();
    }

    /* access modifiers changed from: package-private */
    public boolean isFinancedDevice() {
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
            if (devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return true;
            }
        }
        return false;
    }
}
