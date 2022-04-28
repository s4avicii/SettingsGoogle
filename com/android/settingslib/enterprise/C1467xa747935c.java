package com.android.settingslib.enterprise;

import android.content.Context;
import android.os.UserHandle;
import com.android.settingslib.enterprise.ManagedDeviceActionDisabledByAdminController;

/* renamed from: com.android.settingslib.enterprise.ManagedDeviceActionDisabledByAdminController$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1467xa747935c implements ManagedDeviceActionDisabledByAdminController.ForegroundUserChecker {
    public static final /* synthetic */ C1467xa747935c INSTANCE = new C1467xa747935c();

    private /* synthetic */ C1467xa747935c() {
    }

    public final boolean isUserForeground(Context context, UserHandle userHandle) {
        return ManagedDeviceActionDisabledByAdminController.isUserForeground(context, userHandle);
    }
}
