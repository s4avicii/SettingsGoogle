package com.android.settings.fuelgauge;

import android.app.AppGlobals;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.BatteryConsumer;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UidBatteryConsumer;
import android.os.UserBatteryConsumer;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.DebugUtils;
import android.util.Log;
import androidx.window.C0444R;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.RecentAppOpsAccess;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class BatteryEntry {
    public static final Comparator<BatteryEntry> COMPARATOR = BatteryEntry$$ExternalSyntheticLambda0.INSTANCE;
    private static NameAndIconLoader mRequestThread;
    static Locale sCurrentLocale = null;
    static Handler sHandler;
    static final ArrayList<BatteryEntry> sRequestQueue = new ArrayList<>();
    static final HashMap<String, UidToDetail> sUidCache = new HashMap<>();
    public Drawable icon;
    public int iconId;
    private final BatteryConsumer mBatteryConsumer;
    private double mConsumedPower;
    private final int mConsumerType;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public String mDefaultPackageName;
    private final boolean mIsHidden;
    private final int mPowerComponentId;
    private long mTimeInBackgroundMs;
    private long mTimeInForegroundMs;
    private final int mUid;
    private long mUsageDurationMs;
    public String name;
    public double percent;

    public static final class NameAndIcon {
        public final Drawable icon;
        public final int iconId;
        public final String name;
        public final String packageName;

        public NameAndIcon(String str, Drawable drawable, int i) {
            this(str, (String) null, drawable, i);
        }

        public NameAndIcon(String str, String str2, Drawable drawable, int i) {
            this.name = str;
            this.icon = drawable;
            this.iconId = i;
            this.packageName = str2;
        }
    }

    private static class NameAndIconLoader extends Thread {
        private boolean mAbort = false;

        public NameAndIconLoader() {
            super("BatteryUsage Icon Loader");
        }

        public void abort() {
            this.mAbort = true;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
            r0 = com.android.settings.fuelgauge.BatteryEntry.loadNameAndIcon(com.android.settings.fuelgauge.BatteryEntry.m751$$Nest$fgetmContext(r1), r1.getUid(), com.android.settings.fuelgauge.BatteryEntry.sHandler, r1, com.android.settings.fuelgauge.BatteryEntry.m752$$Nest$fgetmDefaultPackageName(r1), r1.name, r1.icon);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
            if (r0 == null) goto L_0x0000;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
            r1.icon = r0.icon;
            r1.name = r0.name;
            com.android.settings.fuelgauge.BatteryEntry.m753$$Nest$fputmDefaultPackageName(r1, r0.packageName);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r9 = this;
            L_0x0000:
                java.util.ArrayList<com.android.settings.fuelgauge.BatteryEntry> r0 = com.android.settings.fuelgauge.BatteryEntry.sRequestQueue
                monitor-enter(r0)
                boolean r1 = r0.isEmpty()     // Catch:{ all -> 0x0047 }
                if (r1 != 0) goto L_0x003d
                boolean r1 = r9.mAbort     // Catch:{ all -> 0x0047 }
                if (r1 == 0) goto L_0x000e
                goto L_0x003d
            L_0x000e:
                r1 = 0
                java.lang.Object r1 = r0.remove(r1)     // Catch:{ all -> 0x0047 }
                com.android.settings.fuelgauge.BatteryEntry r1 = (com.android.settings.fuelgauge.BatteryEntry) r1     // Catch:{ all -> 0x0047 }
                monitor-exit(r0)     // Catch:{ all -> 0x0047 }
                android.content.Context r2 = r1.mContext
                int r3 = r1.getUid()
                android.os.Handler r4 = com.android.settings.fuelgauge.BatteryEntry.sHandler
                java.lang.String r6 = r1.mDefaultPackageName
                java.lang.String r7 = r1.name
                android.graphics.drawable.Drawable r8 = r1.icon
                r5 = r1
                com.android.settings.fuelgauge.BatteryEntry$NameAndIcon r0 = com.android.settings.fuelgauge.BatteryEntry.loadNameAndIcon(r2, r3, r4, r5, r6, r7, r8)
                if (r0 == 0) goto L_0x0000
                android.graphics.drawable.Drawable r2 = r0.icon
                r1.icon = r2
                java.lang.String r2 = r0.name
                r1.name = r2
                java.lang.String r0 = r0.packageName
                r1.mDefaultPackageName = r0
                goto L_0x0000
            L_0x003d:
                android.os.Handler r9 = com.android.settings.fuelgauge.BatteryEntry.sHandler     // Catch:{ all -> 0x0047 }
                if (r9 == 0) goto L_0x0045
                r1 = 2
                r9.sendEmptyMessage(r1)     // Catch:{ all -> 0x0047 }
            L_0x0045:
                monitor-exit(r0)     // Catch:{ all -> 0x0047 }
                return
            L_0x0047:
                r9 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0047 }
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.BatteryEntry.NameAndIconLoader.run():void");
        }
    }

    public static void startRequestQueue() {
        if (sHandler != null) {
            ArrayList<BatteryEntry> arrayList = sRequestQueue;
            synchronized (arrayList) {
                if (!arrayList.isEmpty()) {
                    NameAndIconLoader nameAndIconLoader = mRequestThread;
                    if (nameAndIconLoader != null) {
                        nameAndIconLoader.abort();
                    }
                    NameAndIconLoader nameAndIconLoader2 = new NameAndIconLoader();
                    mRequestThread = nameAndIconLoader2;
                    nameAndIconLoader2.setPriority(1);
                    mRequestThread.start();
                    arrayList.notify();
                }
            }
        }
    }

    public static void stopRequestQueue() {
        ArrayList<BatteryEntry> arrayList = sRequestQueue;
        synchronized (arrayList) {
            NameAndIconLoader nameAndIconLoader = mRequestThread;
            if (nameAndIconLoader != null) {
                nameAndIconLoader.abort();
                mRequestThread = null;
                arrayList.clear();
                sHandler = null;
            }
        }
    }

    public static void clearUidCache() {
        sUidCache.clear();
    }

    static class UidToDetail {
        Drawable icon;
        String name;
        String packageName;

        UidToDetail() {
        }
    }

    public BatteryEntry(Context context, Handler handler, UserManager userManager, BatteryConsumer batteryConsumer, boolean z, int i, String[] strArr, String str) {
        this(context, handler, userManager, batteryConsumer, z, i, strArr, str, true);
    }

    public BatteryEntry(Context context, Handler handler, UserManager userManager, BatteryConsumer batteryConsumer, boolean z, int i, String[] strArr, String str, boolean z2) {
        sHandler = handler;
        this.mContext = context;
        this.mBatteryConsumer = batteryConsumer;
        this.mIsHidden = z;
        this.mDefaultPackageName = str;
        this.mPowerComponentId = -1;
        if (batteryConsumer instanceof UidBatteryConsumer) {
            this.mUid = i;
            this.mConsumerType = 1;
            this.mConsumedPower = batteryConsumer.getConsumedPower();
            UidBatteryConsumer uidBatteryConsumer = (UidBatteryConsumer) batteryConsumer;
            if (this.mDefaultPackageName == null) {
                if (strArr == null || strArr.length != 1) {
                    this.mDefaultPackageName = uidBatteryConsumer.getPackageWithHighestDrain();
                } else {
                    this.mDefaultPackageName = strArr[0];
                }
            }
            if (this.mDefaultPackageName != null) {
                PackageManager packageManager = context.getPackageManager();
                try {
                    this.name = packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mDefaultPackageName, 0)).toString();
                } catch (PackageManager.NameNotFoundException unused) {
                    Log.d("BatteryEntry", "PackageManager failed to retrieve ApplicationInfo for: " + this.mDefaultPackageName);
                    this.name = this.mDefaultPackageName;
                }
            }
            getQuickNameIconForUid(i, strArr, z2);
            this.mTimeInForegroundMs = uidBatteryConsumer.getTimeInStateMs(0);
            this.mTimeInBackgroundMs = uidBatteryConsumer.getTimeInStateMs(1);
        } else if (batteryConsumer instanceof UserBatteryConsumer) {
            this.mUid = -1;
            this.mConsumerType = 2;
            this.mConsumedPower = batteryConsumer.getConsumedPower();
            NameAndIcon nameAndIconFromUserId = getNameAndIconFromUserId(context, ((UserBatteryConsumer) batteryConsumer).getUserId());
            this.icon = nameAndIconFromUserId.icon;
            this.name = nameAndIconFromUserId.name;
        } else {
            throw new IllegalArgumentException("Unsupported battery consumer: " + batteryConsumer);
        }
    }

    public BatteryEntry(Context context, int i, double d, double d2, long j) {
        this.mContext = context;
        this.mBatteryConsumer = null;
        this.mUid = -1;
        this.mIsHidden = false;
        this.mPowerComponentId = i;
        this.mConsumedPower = i != 0 ? d - d2 : d;
        this.mUsageDurationMs = j;
        this.mConsumerType = 3;
        NameAndIcon nameAndIconFromPowerComponent = getNameAndIconFromPowerComponent(context, i);
        int i2 = nameAndIconFromPowerComponent.iconId;
        this.iconId = i2;
        this.name = nameAndIconFromPowerComponent.name;
        if (i2 != 0) {
            this.icon = context.getDrawable(i2);
        }
    }

    public BatteryEntry(Context context, int i, String str, double d, double d2) {
        this.mContext = context;
        this.mBatteryConsumer = null;
        this.mUid = -1;
        this.mIsHidden = false;
        this.mPowerComponentId = i;
        this.iconId = C0444R.C0447drawable.ic_power_system;
        this.icon = context.getDrawable(C0444R.C0447drawable.ic_power_system);
        this.name = str;
        this.mConsumedPower = i != 0 ? d - d2 : d;
        this.mConsumerType = 3;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public String getLabel() {
        return this.name;
    }

    public int getConsumerType() {
        return this.mConsumerType;
    }

    public int getPowerComponentId() {
        return this.mPowerComponentId;
    }

    /* access modifiers changed from: package-private */
    public void getQuickNameIconForUid(int i, String[] strArr, boolean z) {
        Locale locale = Locale.getDefault();
        if (sCurrentLocale != locale) {
            clearUidCache();
            sCurrentLocale = locale;
        }
        String num = Integer.toString(i);
        HashMap<String, UidToDetail> hashMap = sUidCache;
        if (hashMap.containsKey(num)) {
            UidToDetail uidToDetail = hashMap.get(num);
            this.mDefaultPackageName = uidToDetail.packageName;
            this.name = uidToDetail.name;
            this.icon = uidToDetail.icon;
            return;
        }
        if (strArr == null || strArr.length == 0) {
            NameAndIcon nameAndIconFromUid = getNameAndIconFromUid(this.mContext, this.name, i);
            this.icon = nameAndIconFromUid.icon;
            this.name = nameAndIconFromUid.name;
        } else {
            this.icon = this.mContext.getPackageManager().getDefaultActivityIcon();
        }
        if (sHandler != null && z) {
            ArrayList<BatteryEntry> arrayList = sRequestQueue;
            synchronized (arrayList) {
                arrayList.add(this);
            }
        }
    }

    public static NameAndIcon loadNameAndIcon(Context context, int i, Handler handler, BatteryEntry batteryEntry, String str, String str2, Drawable drawable) {
        String[] strArr;
        Drawable drawable2;
        String str3;
        String str4;
        String str5;
        String str6;
        CharSequence text;
        String charSequence;
        int i2 = i;
        Handler handler2 = handler;
        if (i2 == 0 || i2 == -1) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        if (i2 == 1000) {
            strArr = new String[]{RecentAppOpsAccess.ANDROID_SYSTEM_PACKAGE_NAME};
        } else {
            strArr = packageManager.getPackagesForUid(i2);
        }
        String[] strArr2 = strArr;
        if (strArr2 != null) {
            int length = strArr2.length;
            String[] strArr3 = new String[length];
            System.arraycopy(strArr2, 0, strArr3, 0, strArr2.length);
            IPackageManager packageManager2 = AppGlobals.getPackageManager();
            int userId = UserHandle.getUserId(i);
            str3 = str;
            int i3 = 0;
            while (true) {
                str5 = "BatteryEntry";
                if (i3 >= length) {
                    drawable2 = drawable;
                    break;
                }
                try {
                    ApplicationInfo applicationInfo = packageManager2.getApplicationInfo(strArr3[i3], 0, userId);
                    if (applicationInfo == null) {
                        Log.d(str5, "Retrieving null app info for package " + strArr3[i3] + ", user " + userId);
                    } else {
                        CharSequence loadLabel = applicationInfo.loadLabel(packageManager);
                        if (loadLabel != null) {
                            strArr3[i3] = loadLabel.toString();
                        }
                        if (applicationInfo.icon != 0) {
                            str3 = strArr2[i3];
                            drawable2 = applicationInfo.loadIcon(packageManager);
                            break;
                        }
                    }
                } catch (RemoteException e) {
                    Log.d(str5, "Error while retrieving app info for package " + strArr3[i3] + ", user " + userId, e);
                }
                i3++;
            }
            if (length == 1) {
                str4 = strArr3[0];
            } else {
                int length2 = strArr2.length;
                String str7 = str2;
                int i4 = 0;
                while (true) {
                    if (i4 >= length2) {
                        str4 = str7;
                        break;
                    }
                    String str8 = strArr2[i4];
                    String str9 = str5;
                    try {
                        PackageInfo packageInfo = packageManager2.getPackageInfo(str8, 0, userId);
                        if (packageInfo == null) {
                            str6 = str9;
                            try {
                                Log.d(str6, "Retrieving null package info for package " + str8 + ", user " + userId);
                            } catch (RemoteException e2) {
                                e = e2;
                                Log.d(str6, "Error while retrieving package info for package " + str8 + ", user " + userId, e);
                                i4++;
                                int i5 = i;
                                str5 = str6;
                            }
                            i4++;
                            int i52 = i;
                            str5 = str6;
                        } else {
                            str6 = str9;
                            int i6 = packageInfo.sharedUserLabel;
                            if (!(i6 == 0 || (text = packageManager.getText(str8, i6, packageInfo.applicationInfo)) == null)) {
                                charSequence = text.toString();
                                try {
                                    ApplicationInfo applicationInfo2 = packageInfo.applicationInfo;
                                    if (applicationInfo2.icon == 0) {
                                        break;
                                    }
                                    try {
                                        drawable2 = applicationInfo2.loadIcon(packageManager);
                                        str3 = str8;
                                        break;
                                    } catch (RemoteException e3) {
                                        e = e3;
                                        str7 = charSequence;
                                        str3 = str8;
                                        Log.d(str6, "Error while retrieving package info for package " + str8 + ", user " + userId, e);
                                        i4++;
                                        int i522 = i;
                                        str5 = str6;
                                    }
                                } catch (RemoteException e4) {
                                    e = e4;
                                    str7 = charSequence;
                                    Log.d(str6, "Error while retrieving package info for package " + str8 + ", user " + userId, e);
                                    i4++;
                                    int i5222 = i;
                                    str5 = str6;
                                }
                            }
                            i4++;
                            int i52222 = i;
                            str5 = str6;
                        }
                    } catch (RemoteException e5) {
                        e = e5;
                        str6 = str9;
                        Log.d(str6, "Error while retrieving package info for package " + str8 + ", user " + userId, e);
                        i4++;
                        int i522222 = i;
                        str5 = str6;
                    }
                }
                str4 = charSequence;
            }
        } else {
            str3 = str;
            str4 = str2;
            drawable2 = drawable;
        }
        String num = Integer.toString(i);
        if (drawable2 == null) {
            drawable2 = packageManager.getDefaultActivityIcon();
        }
        UidToDetail uidToDetail = new UidToDetail();
        uidToDetail.name = str4;
        uidToDetail.icon = drawable2;
        uidToDetail.packageName = str3;
        sUidCache.put(num, uidToDetail);
        if (handler2 != null) {
            handler2.sendMessage(handler2.obtainMessage(1, batteryEntry));
        }
        return new NameAndIcon(str4, str3, drawable2, 0);
    }

    public String getKey() {
        BatteryConsumer batteryConsumer = this.mBatteryConsumer;
        if (batteryConsumer instanceof UidBatteryConsumer) {
            return Integer.toString(this.mUid);
        }
        if (batteryConsumer instanceof UserBatteryConsumer) {
            return "U|" + this.mBatteryConsumer.getUserId();
        }
        return "S|" + this.mPowerComponentId;
    }

    public boolean isHidden() {
        return this.mIsHidden;
    }

    public boolean isAppEntry() {
        return this.mBatteryConsumer instanceof UidBatteryConsumer;
    }

    public boolean isUserEntry() {
        return this.mBatteryConsumer instanceof UserBatteryConsumer;
    }

    public String getDefaultPackageName() {
        return this.mDefaultPackageName;
    }

    public int getUid() {
        return this.mUid;
    }

    public long getTimeInForegroundMs() {
        if (this.mBatteryConsumer instanceof UidBatteryConsumer) {
            return this.mTimeInForegroundMs;
        }
        return this.mUsageDurationMs;
    }

    public long getTimeInBackgroundMs() {
        if (this.mBatteryConsumer instanceof UidBatteryConsumer) {
            return this.mTimeInBackgroundMs;
        }
        return 0;
    }

    public double getConsumedPower() {
        return this.mConsumedPower;
    }

    public void add(BatteryConsumer batteryConsumer) {
        this.mConsumedPower += batteryConsumer.getConsumedPower();
        if (batteryConsumer instanceof UidBatteryConsumer) {
            UidBatteryConsumer uidBatteryConsumer = (UidBatteryConsumer) batteryConsumer;
            this.mTimeInForegroundMs += uidBatteryConsumer.getTimeInStateMs(0);
            this.mTimeInBackgroundMs += uidBatteryConsumer.getTimeInStateMs(1);
            if (this.mDefaultPackageName == null) {
                this.mDefaultPackageName = uidBatteryConsumer.getPackageWithHighestDrain();
            }
        }
    }

    public static NameAndIcon getNameAndIconFromUserId(Context context, int i) {
        String str;
        Drawable drawable;
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        UserInfo userInfo = userManager.getUserInfo(i);
        if (userInfo != null) {
            drawable = Utils.getUserIcon(context, userManager, userInfo);
            str = Utils.getUserLabel(context, userInfo);
        } else {
            str = context.getResources().getString(C0444R.string.running_process_item_removed_user_label);
            drawable = null;
        }
        return new NameAndIcon(str, drawable, 0);
    }

    public static NameAndIcon getNameAndIconFromUid(Context context, String str, int i) {
        Drawable drawable = context.getDrawable(C0444R.C0447drawable.ic_power_system);
        if (i == 0) {
            str = context.getResources().getString(C0444R.string.process_kernel_label);
        } else if ("mediaserver".equals(str)) {
            str = context.getResources().getString(C0444R.string.process_mediaserver_label);
        } else if ("dex2oat".equals(str) || "dex2oat32".equals(str) || "dex2oat64".equals(str)) {
            str = context.getResources().getString(C0444R.string.process_dex2oat_label);
        }
        return new NameAndIcon(str, drawable, 0);
    }

    public static NameAndIcon getNameAndIconFromPowerComponent(Context context, int i) {
        String str;
        int i2 = C0444R.C0447drawable.ic_settings_display;
        if (i == 0) {
            str = context.getResources().getString(C0444R.string.power_screen);
        } else if (i == 6) {
            str = context.getResources().getString(C0444R.string.power_flashlight);
        } else if (i == 8) {
            str = context.getResources().getString(C0444R.string.power_cell);
            i2 = C0444R.C0447drawable.ic_cellular_1_bar;
        } else if (i == 11) {
            str = context.getResources().getString(C0444R.string.power_wifi);
            i2 = C0444R.C0447drawable.ic_settings_wireless;
        } else if (i == 2) {
            str = context.getResources().getString(C0444R.string.power_bluetooth);
            i2 = 17302841;
        } else if (i != 3) {
            switch (i) {
                case 13:
                case 16:
                    str = context.getResources().getString(C0444R.string.power_idle);
                    i2 = C0444R.C0447drawable.ic_settings_phone_idle;
                    break;
                case 14:
                    str = context.getResources().getString(C0444R.string.power_phone);
                    i2 = C0444R.C0447drawable.ic_settings_voice_calls;
                    break;
                case 15:
                    str = context.getResources().getString(C0444R.string.ambient_display_screen_title);
                    i2 = C0444R.C0447drawable.ic_settings_aod;
                    break;
                default:
                    str = DebugUtils.constantToString(BatteryConsumer.class, "POWER_COMPONENT_", i);
                    i2 = C0444R.C0447drawable.ic_power_system;
                    break;
            }
        } else {
            str = context.getResources().getString(C0444R.string.power_camera);
            i2 = C0444R.C0447drawable.ic_settings_camera;
        }
        return new NameAndIcon(str, (Drawable) null, i2);
    }
}
