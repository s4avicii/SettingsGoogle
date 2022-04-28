package com.android.settings.location;

import android.permission.PermissionControllerManager;

/* renamed from: com.android.settings.location.AppLocationPermissionPreferenceController$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0996xdc12bf8b implements PermissionControllerManager.OnCountPermissionAppsResultCallback {
    public final /* synthetic */ AppLocationPermissionPreferenceController f$0;

    public /* synthetic */ C0996xdc12bf8b(AppLocationPermissionPreferenceController appLocationPermissionPreferenceController) {
        this.f$0 = appLocationPermissionPreferenceController;
    }

    public final void onCountPermissionApps(int i) {
        this.f$0.lambda$updateState$0(i);
    }
}
