package com.android.settings.applications;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.window.C0444R;
import com.android.settingslib.applications.ApplicationsState;

public class AppLocaleUtil {
    private static final String TAG = "AppLocaleUtil";

    public static boolean canDisplayLocaleUi(Context context, ApplicationsState.AppEntry appEntry) {
        return !isDisallowedPackage(context, appEntry.info.packageName) && !isSignedWithPlatformKey(context, appEntry.info.packageName) && appEntry.hasLauncherEntry;
    }

    private static boolean isDisallowedPackage(Context context, String str) {
        for (String equals : context.getResources().getStringArray(C0444R.array.config_disallowed_app_localeChange_packages)) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSignedWithPlatformKey(Context context, String str) {
        PackageInfo packageInfo;
        PackageManager packageManager = context.getPackageManager();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
        try {
            packageInfo = packageManager.getPackageInfoAsUser(str, 0, ActivityManager.getCurrentUser());
        } catch (PackageManager.NameNotFoundException unused) {
            String str2 = TAG;
            Log.e(str2, "package not found: " + str);
            packageInfo = null;
        }
        if (packageInfo == null) {
            return false;
        }
        return packageInfo.applicationInfo.isSignedWithPlatformKey();
    }
}
