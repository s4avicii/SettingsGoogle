package com.android.settings.display.darkmode;

import android.content.Context;
import android.content.pm.PackageManager;

public final class BedtimeSettings {
    private final Context mContext;
    private final PackageManager mPackageManager;
    private final String mWellbeingPackage;

    public BedtimeSettings(Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mWellbeingPackage = context.getResources().getString(17039936);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000d, code lost:
        r0 = new android.content.Intent("android.settings.BEDTIME_SETTINGS").setPackage(r3.mWellbeingPackage);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.content.Intent getBedtimeSettingsIntent() {
        /*
            r3 = this;
            android.content.Context r0 = r3.mContext
            java.lang.String r1 = "settings_app_allow_dark_theme_activation_at_bedtime"
            boolean r0 = android.util.FeatureFlagUtils.isEnabled(r0, r1)
            r1 = 0
            if (r0 != 0) goto L_0x000d
            return r1
        L_0x000d:
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r2 = "android.settings.BEDTIME_SETTINGS"
            r0.<init>(r2)
            java.lang.String r2 = r3.mWellbeingPackage
            android.content.Intent r0 = r0.setPackage(r2)
            android.content.pm.PackageManager r3 = r3.mPackageManager
            r2 = 65536(0x10000, float:9.18355E-41)
            android.content.pm.ResolveInfo r3 = r3.resolveActivity(r0, r2)
            if (r3 == 0) goto L_0x002d
            android.content.pm.ActivityInfo r3 = r3.activityInfo
            boolean r3 = r3.isEnabled()
            if (r3 == 0) goto L_0x002d
            return r0
        L_0x002d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.display.darkmode.BedtimeSettings.getBedtimeSettingsIntent():android.content.Intent");
    }
}
