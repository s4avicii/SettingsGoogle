package com.android.settings.enterprise;

import com.android.settings.applications.ApplicationFeatureProvider;

/* renamed from: com.android.settings.enterprise.AdminGrantedPermissionsPreferenceControllerBase$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0910x5f15e790 implements ApplicationFeatureProvider.NumberOfAppsCallback {
    public final /* synthetic */ Boolean[] f$0;

    public /* synthetic */ C0910x5f15e790(Boolean[] boolArr) {
        this.f$0 = boolArr;
    }

    public final void onNumberOfAppsResult(int i) {
        AdminGrantedPermissionsPreferenceControllerBase.lambda$isAvailable$1(this.f$0, i);
    }
}
