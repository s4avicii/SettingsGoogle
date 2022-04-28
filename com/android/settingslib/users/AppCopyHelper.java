package com.android.settingslib.users;

import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class AppCopyHelper {
    private final IPackageManager mIPm;
    private boolean mLeanback;
    private final PackageManager mPackageManager;
    private final ArraySet<String> mSelectedPackages;
    private final UserHandle mUser;
    private List<SelectableAppInfo> mVisibleApps;

    public AppCopyHelper(Context context, UserHandle userHandle) {
        this(new Injector(context, userHandle));
    }

    AppCopyHelper(Injector injector) {
        this.mSelectedPackages = new ArraySet<>();
        this.mPackageManager = injector.getPackageManager();
        this.mIPm = injector.getIPackageManager();
        this.mUser = injector.getUser();
    }

    public void setPackageSelected(String str, boolean z) {
        if (z) {
            this.mSelectedPackages.add(str);
        } else {
            this.mSelectedPackages.remove(str);
        }
    }

    public void resetSelectedPackages() {
        this.mSelectedPackages.clear();
    }

    public List<SelectableAppInfo> getVisibleApps() {
        return this.mVisibleApps;
    }

    public void installSelectedApps() {
        for (int i = 0; i < this.mSelectedPackages.size(); i++) {
            installSelectedApp(this.mSelectedPackages.valueAt(i));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001c, code lost:
        if ((r1.flags & 8388608) != 0) goto L_0x0042;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void installSelectedApp(java.lang.String r12) {
        /*
            r11 = this;
            android.os.UserHandle r0 = r11.mUser
            int r0 = r0.getIdentifier()
            android.content.pm.IPackageManager r1 = r11.mIPm     // Catch:{ RemoteException -> 0x0069 }
            r2 = 4194304(0x400000, double:2.0722615E-317)
            android.content.pm.ApplicationInfo r1 = r1.getApplicationInfo(r12, r2, r0)     // Catch:{ RemoteException -> 0x0069 }
            r2 = 8388608(0x800000, float:1.17549435E-38)
            java.lang.String r3 = "AppCopyHelper"
            if (r1 == 0) goto L_0x001e
            boolean r4 = r1.enabled     // Catch:{ RemoteException -> 0x0069 }
            if (r4 == 0) goto L_0x001e
            int r4 = r1.flags     // Catch:{ RemoteException -> 0x0069 }
            r4 = r4 & r2
            if (r4 != 0) goto L_0x0042
        L_0x001e:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0069 }
            r4.<init>()     // Catch:{ RemoteException -> 0x0069 }
            java.lang.String r5 = "Installing "
            r4.append(r5)     // Catch:{ RemoteException -> 0x0069 }
            r4.append(r12)     // Catch:{ RemoteException -> 0x0069 }
            java.lang.String r4 = r4.toString()     // Catch:{ RemoteException -> 0x0069 }
            android.util.Log.i(r3, r4)     // Catch:{ RemoteException -> 0x0069 }
            android.content.pm.IPackageManager r5 = r11.mIPm     // Catch:{ RemoteException -> 0x0069 }
            android.os.UserHandle r4 = r11.mUser     // Catch:{ RemoteException -> 0x0069 }
            int r7 = r4.getIdentifier()     // Catch:{ RemoteException -> 0x0069 }
            r8 = 4194304(0x400000, float:5.877472E-39)
            r9 = 0
            r10 = 0
            r6 = r12
            r5.installExistingPackageAsUser(r6, r7, r8, r9, r10)     // Catch:{ RemoteException -> 0x0069 }
        L_0x0042:
            if (r1 == 0) goto L_0x0069
            int r4 = r1.privateFlags     // Catch:{ RemoteException -> 0x0069 }
            r4 = r4 & 1
            if (r4 == 0) goto L_0x0069
            int r1 = r1.flags     // Catch:{ RemoteException -> 0x0069 }
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0069
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0069 }
            r1.<init>()     // Catch:{ RemoteException -> 0x0069 }
            java.lang.String r2 = "Unhiding "
            r1.append(r2)     // Catch:{ RemoteException -> 0x0069 }
            r1.append(r12)     // Catch:{ RemoteException -> 0x0069 }
            java.lang.String r1 = r1.toString()     // Catch:{ RemoteException -> 0x0069 }
            android.util.Log.i(r3, r1)     // Catch:{ RemoteException -> 0x0069 }
            android.content.pm.IPackageManager r11 = r11.mIPm     // Catch:{ RemoteException -> 0x0069 }
            r1 = 0
            r11.setApplicationHiddenSettingAsUser(r12, r1, r0)     // Catch:{ RemoteException -> 0x0069 }
        L_0x0069:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.AppCopyHelper.installSelectedApp(java.lang.String):void");
    }

    public void fetchAndMergeApps() {
        this.mVisibleApps = new ArrayList();
        addCurrentUsersApps();
        removeSecondUsersApp();
    }

    private void addCurrentUsersApps() {
        addSystemApps(this.mVisibleApps, new Intent("android.intent.action.MAIN").addCategory(this.mLeanback ? "android.intent.category.LEANBACK_LAUNCHER" : "android.intent.category.LAUNCHER"));
        addSystemApps(this.mVisibleApps, new Intent("android.appwidget.action.APPWIDGET_UPDATE"));
        for (ApplicationInfo next : this.mPackageManager.getInstalledApplications(0)) {
            int i = next.flags;
            if ((8388608 & i) != 0 && (i & 1) == 0 && (i & 128) == 0) {
                SelectableAppInfo selectableAppInfo = new SelectableAppInfo();
                selectableAppInfo.packageName = next.packageName;
                selectableAppInfo.appName = next.loadLabel(this.mPackageManager);
                selectableAppInfo.icon = next.loadIcon(this.mPackageManager);
                this.mVisibleApps.add(selectableAppInfo);
            }
        }
        HashSet hashSet = new HashSet();
        for (int size = this.mVisibleApps.size() - 1; size >= 0; size--) {
            SelectableAppInfo selectableAppInfo2 = this.mVisibleApps.get(size);
            if (TextUtils.isEmpty(selectableAppInfo2.packageName) || !hashSet.contains(selectableAppInfo2.packageName)) {
                hashSet.add(selectableAppInfo2.packageName);
            } else {
                this.mVisibleApps.remove(size);
            }
        }
        this.mVisibleApps.sort(new AppLabelComparator());
    }

    private void removeSecondUsersApp() {
        HashSet hashSet = new HashSet();
        List installedApplicationsAsUser = this.mPackageManager.getInstalledApplicationsAsUser(8192, this.mUser.getIdentifier());
        for (int size = installedApplicationsAsUser.size() - 1; size >= 0; size--) {
            ApplicationInfo applicationInfo = (ApplicationInfo) installedApplicationsAsUser.get(size);
            if ((applicationInfo.flags & 8388608) != 0) {
                hashSet.add(applicationInfo.packageName);
            }
        }
        for (int size2 = this.mVisibleApps.size() - 1; size2 >= 0; size2--) {
            SelectableAppInfo selectableAppInfo = this.mVisibleApps.get(size2);
            if (!TextUtils.isEmpty(selectableAppInfo.packageName) && hashSet.contains(selectableAppInfo.packageName)) {
                this.mVisibleApps.remove(size2);
            }
        }
    }

    private void addSystemApps(List<SelectableAppInfo> list, Intent intent) {
        ApplicationInfo applicationInfo;
        for (ResolveInfo next : this.mPackageManager.queryIntentActivities(intent, 0)) {
            ActivityInfo activityInfo = next.activityInfo;
            if (!(activityInfo == null || (applicationInfo = activityInfo.applicationInfo) == null)) {
                int i = applicationInfo.flags;
                if ((i & 1) != 0 || (i & 128) != 0) {
                    SelectableAppInfo selectableAppInfo = new SelectableAppInfo();
                    ActivityInfo activityInfo2 = next.activityInfo;
                    selectableAppInfo.packageName = activityInfo2.packageName;
                    selectableAppInfo.appName = activityInfo2.applicationInfo.loadLabel(this.mPackageManager);
                    selectableAppInfo.icon = next.activityInfo.loadIcon(this.mPackageManager);
                    list.add(selectableAppInfo);
                }
            }
        }
    }

    public static class SelectableAppInfo {
        public CharSequence appName;
        public Drawable icon;
        public String packageName;

        public String toString() {
            return this.packageName + ": appName=" + this.appName + "; icon=" + this.icon;
        }
    }

    private static class AppLabelComparator implements Comparator<SelectableAppInfo> {
        private AppLabelComparator() {
        }

        public int compare(SelectableAppInfo selectableAppInfo, SelectableAppInfo selectableAppInfo2) {
            return selectableAppInfo.appName.toString().toLowerCase().compareTo(selectableAppInfo2.appName.toString().toLowerCase());
        }
    }

    static class Injector {
        private final Context mContext;
        private final UserHandle mUser;

        Injector(Context context, UserHandle userHandle) {
            this.mContext = context;
            this.mUser = userHandle;
        }

        /* access modifiers changed from: package-private */
        public UserHandle getUser() {
            return this.mUser;
        }

        /* access modifiers changed from: package-private */
        public PackageManager getPackageManager() {
            return this.mContext.getPackageManager();
        }

        /* access modifiers changed from: package-private */
        public IPackageManager getIPackageManager() {
            return AppGlobals.getPackageManager();
        }
    }
}
