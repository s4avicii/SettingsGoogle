package com.android.settings.enterprise;

import com.android.settings.applications.ApplicationFeatureProvider;

/* renamed from: com.android.settings.enterprise.EnterpriseInstalledPackagesPreferenceController$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0914xc1662b8d implements ApplicationFeatureProvider.NumberOfAppsCallback {
    public final /* synthetic */ Boolean[] f$0;

    public /* synthetic */ C0914xc1662b8d(Boolean[] boolArr) {
        this.f$0 = boolArr;
    }

    public final void onNumberOfAppsResult(int i) {
        EnterpriseInstalledPackagesPreferenceController.lambda$isAvailable$1(this.f$0, i);
    }
}
