package com.android.settings.applications;

import android.content.pm.PackageInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppPermissionsPreferenceController$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ AppPermissionsPreferenceController$$ExternalSyntheticLambda0 INSTANCE = new AppPermissionsPreferenceController$$ExternalSyntheticLambda0();

    private /* synthetic */ AppPermissionsPreferenceController$$ExternalSyntheticLambda0() {
    }

    public final boolean test(Object obj) {
        return AppPermissionsPreferenceController.lambda$queryPermissionSummary$0((PackageInfo) obj);
    }
}
