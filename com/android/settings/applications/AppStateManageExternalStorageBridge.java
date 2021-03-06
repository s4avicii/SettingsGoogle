package com.android.settings.applications;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.android.settings.applications.AppStateAppOpsBridge;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settingslib.applications.ApplicationsState;

public class AppStateManageExternalStorageBridge extends AppStateAppOpsBridge {
    public static final ApplicationsState.AppFilter FILTER_MANAGE_EXTERNAL_STORAGE = new ApplicationsState.AppFilter() {
        public void init() {
        }

        public boolean filterApp(ApplicationsState.AppEntry appEntry) {
            return appEntry.extraInfo != null;
        }
    };
    private static final String[] PERMISSIONS = {"android.permission.MANAGE_EXTERNAL_STORAGE"};
    private final AppOpsManager mAppOpsManager;

    public AppStateManageExternalStorageBridge(Context context, ApplicationsState applicationsState, AppStateBaseBridge.Callback callback) {
        super(context, applicationsState, callback, AppOpsManager.strOpToOp("android:manage_external_storage"), PERMISSIONS);
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
    }

    /* access modifiers changed from: protected */
    public void updateExtraInfo(ApplicationsState.AppEntry appEntry, String str, int i) {
        appEntry.extraInfo = getManageExternalStoragePermState(str, i);
    }

    /* access modifiers changed from: protected */
    public void loadAllExtraInfo() {
        super.loadAllExtraInfo();
        for (ApplicationsState.AppEntry next : this.mAppSession.getAllApps()) {
            Object obj = next.extraInfo;
            if (obj instanceof AppStateAppOpsBridge.PermissionState) {
                AppOpsManager appOpsManager = this.mAppOpsManager;
                ApplicationInfo applicationInfo = next.info;
                ((AppStateAppOpsBridge.PermissionState) obj).appOpMode = appOpsManager.unsafeCheckOpNoThrow("android:manage_external_storage", applicationInfo.uid, applicationInfo.packageName);
            }
        }
    }

    public AppStateAppOpsBridge.PermissionState getPermissionInfo(String str, int i) {
        AppStateAppOpsBridge.PermissionState permissionInfo = super.getPermissionInfo(str, i);
        permissionInfo.appOpMode = this.mAppOpsManager.unsafeCheckOpNoThrow("android:manage_external_storage", i, str);
        return permissionInfo;
    }

    public AppStateAppOpsBridge.PermissionState getManageExternalStoragePermState(String str, int i) {
        return getPermissionInfo(str, i);
    }
}
