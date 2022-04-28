package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.UserManager;
import android.util.ArraySet;
import java.util.Set;

class WifiTrackerInjector {
    private final DevicePolicyManager mDevicePolicyManager;
    private final boolean mIsDemoMode;
    private final Set<String> mNoAttributionAnnotationPackages = new ArraySet();
    private final UserManager mUserManager;

    WifiTrackerInjector(Context context) {
        this.mIsDemoMode = HiddenApiWrapper.isDemoMode(context);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        String[] split = context.getString(R$string.wifitrackerlib_no_attribution_annotation_packages).split(",");
        for (String add : split) {
            this.mNoAttributionAnnotationPackages.add(add);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDemoMode() {
        return this.mIsDemoMode;
    }

    public UserManager getUserManager() {
        return this.mUserManager;
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return this.mDevicePolicyManager;
    }

    /* access modifiers changed from: package-private */
    public Set<String> getNoAttributionAnnotationPackages() {
        return this.mNoAttributionAnnotationPackages;
    }
}
