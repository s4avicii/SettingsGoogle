package com.android.settings.enterprise;

import android.content.Context;

public class CaCertsManagedProfilePreferenceController extends CaCertsPreferenceControllerBase {
    static final String CA_CERTS_MANAGED_PROFILE = "ca_certs_managed_profile";

    public String getPreferenceKey() {
        return CA_CERTS_MANAGED_PROFILE;
    }

    public CaCertsManagedProfilePreferenceController(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getNumberOfCaCerts() {
        return this.mFeatureProvider.getNumberOfOwnerInstalledCaCertsForManagedProfile();
    }
}
