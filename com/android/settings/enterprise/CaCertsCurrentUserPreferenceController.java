package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import androidx.preference.Preference;
import androidx.window.C0444R;

public class CaCertsCurrentUserPreferenceController extends CaCertsPreferenceControllerBase {
    static final String CA_CERTS_CURRENT_USER = "ca_certs_current_user";
    DevicePolicyManager mDevicePolicyManager = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class));

    public String getPreferenceKey() {
        return CA_CERTS_CURRENT_USER;
    }

    public CaCertsCurrentUserPreferenceController(Context context) {
        super(context);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (this.mFeatureProvider.isInCompMode()) {
            preference.setTitle((CharSequence) this.mDevicePolicyManager.getString("Settings.CA_CERTS_PERSONAL_PROFILE", new CaCertsCurrentUserPreferenceController$$ExternalSyntheticLambda1(this)));
        } else {
            preference.setTitle((CharSequence) this.mDevicePolicyManager.getString("Settings.CA_CERTS_DEVICE", new CaCertsCurrentUserPreferenceController$$ExternalSyntheticLambda0(this)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateState$0() throws Exception {
        return this.mContext.getString(C0444R.string.enterprise_privacy_ca_certs_personal);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateState$1() throws Exception {
        return this.mContext.getString(C0444R.string.enterprise_privacy_ca_certs_device);
    }

    /* access modifiers changed from: protected */
    public int getNumberOfCaCerts() {
        return this.mFeatureProvider.getNumberOfOwnerInstalledCaCertsForCurrentUser();
    }
}
