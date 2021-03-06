package com.android.settings.fuelgauge;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.settings.fuelgauge.BatteryEntry;
import com.android.settingslib.utils.StringUtil;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BatteryDiffEntry {
    public static final Comparator<BatteryDiffEntry> COMPARATOR = BatteryDiffEntry$$ExternalSyntheticLambda0.INSTANCE;
    static Locale sCurrentLocale;
    static final Map<String, BatteryEntry.NameAndIcon> sResourceCache = new HashMap();
    public static final Map<String, Boolean> sValidForRestriction = new HashMap();
    Drawable mAppIcon = null;
    int mAppIconId;
    String mAppLabel = null;
    public long mBackgroundUsageTimeInMs;
    public final BatteryHistEntry mBatteryHistEntry;
    public double mConsumePower;
    private Context mContext;
    private String mDefaultPackageName = null;
    public long mForegroundUsageTimeInMs;
    boolean mIsLoaded = false;
    private double mPercentOfTotal;
    private double mTotalConsumePower;
    private UserManager mUserManager;
    boolean mValidForRestriction = true;

    public BatteryDiffEntry(Context context, long j, long j2, double d, BatteryHistEntry batteryHistEntry) {
        this.mContext = context;
        this.mConsumePower = d;
        this.mForegroundUsageTimeInMs = j;
        this.mBackgroundUsageTimeInMs = j2;
        this.mBatteryHistEntry = batteryHistEntry;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public void setTotalConsumePower(double d) {
        this.mTotalConsumePower = d;
        double d2 = 0.0d;
        if (d != 0.0d) {
            d2 = (this.mConsumePower / d) * 100.0d;
        }
        this.mPercentOfTotal = d2;
    }

    public double getPercentOfTotal() {
        return this.mPercentOfTotal;
    }

    public BatteryDiffEntry clone() {
        return new BatteryDiffEntry(this.mContext, this.mForegroundUsageTimeInMs, this.mBackgroundUsageTimeInMs, this.mConsumePower, this.mBatteryHistEntry);
    }

    public String getAppLabel() {
        loadLabelAndIcon();
        String str = this.mAppLabel;
        if (str == null || str.length() == 0) {
            return this.mBatteryHistEntry.mAppLabel;
        }
        return this.mAppLabel;
    }

    public Drawable getAppIcon() {
        loadLabelAndIcon();
        Drawable drawable = this.mAppIcon;
        if (drawable == null || drawable.getConstantState() == null) {
            return null;
        }
        return this.mAppIcon.getConstantState().newDrawable();
    }

    public int getAppIconId() {
        loadLabelAndIcon();
        return this.mAppIconId;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000c, code lost:
        r2 = r0.split(":");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getPackageName() {
        /*
            r2 = this;
            java.lang.String r0 = r2.mDefaultPackageName
            if (r0 == 0) goto L_0x0005
            goto L_0x0009
        L_0x0005:
            com.android.settings.fuelgauge.BatteryHistEntry r2 = r2.mBatteryHistEntry
            java.lang.String r0 = r2.mPackageName
        L_0x0009:
            if (r0 != 0) goto L_0x000c
            return r0
        L_0x000c:
            java.lang.String r2 = ":"
            java.lang.String[] r2 = r0.split(r2)
            if (r2 == 0) goto L_0x001a
            int r1 = r2.length
            if (r1 <= 0) goto L_0x001a
            r0 = 0
            r0 = r2[r0]
        L_0x001a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.fuelgauge.BatteryDiffEntry.getPackageName():java.lang.String");
    }

    public boolean validForRestriction() {
        loadLabelAndIcon();
        return this.mValidForRestriction;
    }

    public boolean isSystemEntry() {
        BatteryHistEntry batteryHistEntry = this.mBatteryHistEntry;
        int i = batteryHistEntry.mConsumerType;
        if (i != 1) {
            return i == 2 || i == 3;
        }
        if (isSystemUid((int) batteryHistEntry.mUid) || this.mBatteryHistEntry.mIsHidden) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void loadLabelAndIcon() {
        BatteryEntry.NameAndIcon nameAndIconFromPowerComponent;
        if (!this.mIsLoaded) {
            BatteryEntry.NameAndIcon cache = getCache();
            if (cache != null) {
                this.mAppLabel = cache.name;
                this.mAppIcon = cache.icon;
                this.mAppIconId = cache.iconId;
            }
            Map<String, Boolean> map = sValidForRestriction;
            Boolean bool = map.get(getKey());
            if (bool != null) {
                this.mValidForRestriction = bool.booleanValue();
            }
            if (cache == null || bool == null) {
                this.mIsLoaded = true;
                updateRestrictionFlagState();
                map.put(getKey(), Boolean.valueOf(this.mValidForRestriction));
                BatteryHistEntry batteryHistEntry = this.mBatteryHistEntry;
                int i = batteryHistEntry.mConsumerType;
                if (i == 1) {
                    loadNameAndIconForUid();
                    if (this.mAppIcon == null) {
                        this.mAppIcon = this.mContext.getPackageManager().getDefaultActivityIcon();
                    }
                    Drawable badgeIconForUser = getBadgeIconForUser(this.mAppIcon);
                    this.mAppIcon = badgeIconForUser;
                    if (this.mAppLabel != null || badgeIconForUser != null) {
                        sResourceCache.put(getKey(), new BatteryEntry.NameAndIcon(this.mAppLabel, this.mAppIcon, 0));
                    }
                } else if (i == 2) {
                    BatteryEntry.NameAndIcon nameAndIconFromUserId = BatteryEntry.getNameAndIconFromUserId(this.mContext, (int) batteryHistEntry.mUserId);
                    if (nameAndIconFromUserId != null) {
                        this.mAppIcon = nameAndIconFromUserId.icon;
                        this.mAppLabel = nameAndIconFromUserId.name;
                        sResourceCache.put(getKey(), new BatteryEntry.NameAndIcon(this.mAppLabel, this.mAppIcon, 0));
                    }
                } else if (i == 3 && (nameAndIconFromPowerComponent = BatteryEntry.getNameAndIconFromPowerComponent(this.mContext, batteryHistEntry.mDrainType)) != null) {
                    this.mAppLabel = nameAndIconFromPowerComponent.name;
                    int i2 = nameAndIconFromPowerComponent.iconId;
                    if (i2 != 0) {
                        this.mAppIconId = i2;
                        this.mAppIcon = this.mContext.getDrawable(i2);
                    }
                    sResourceCache.put(getKey(), new BatteryEntry.NameAndIcon(this.mAppLabel, this.mAppIcon, this.mAppIconId));
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String getKey() {
        return this.mBatteryHistEntry.getKey();
    }

    /* access modifiers changed from: package-private */
    public void updateRestrictionFlagState() {
        this.mValidForRestriction = true;
        if (this.mBatteryHistEntry.isAppEntry()) {
            if (!(BatteryUtils.getInstance(this.mContext).getPackageUid(getPackageName()) != -1)) {
                this.mValidForRestriction = false;
                return;
            }
            try {
                this.mValidForRestriction = this.mContext.getPackageManager().getPackageInfo(getPackageName(), 4198976) != null;
            } catch (Exception e) {
                Log.e("BatteryDiffEntry", String.format("getPackageInfo() error %s for package=%s", new Object[]{e.getCause(), getPackageName()}));
                this.mValidForRestriction = false;
            }
        }
    }

    private BatteryEntry.NameAndIcon getCache() {
        Locale locale = Locale.getDefault();
        Locale locale2 = sCurrentLocale;
        if (locale2 != locale) {
            Log.d("BatteryDiffEntry", String.format("clearCache() locale is changed from %s to %s", new Object[]{locale2, locale}));
            sCurrentLocale = locale;
            clearCache();
        }
        return sResourceCache.get(getKey());
    }

    private void loadNameAndIconForUid() {
        String packageName = getPackageName();
        PackageManager packageManager = this.mContext.getPackageManager();
        if (!(packageName == null || packageName.length() == 0)) {
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                if (applicationInfo != null) {
                    this.mAppLabel = packageManager.getApplicationLabel(applicationInfo).toString();
                    this.mAppIcon = packageManager.getApplicationIcon(applicationInfo);
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e("BatteryDiffEntry", "failed to retrieve ApplicationInfo for: " + packageName);
                this.mAppLabel = packageName;
            }
        }
        if (this.mAppLabel == null || this.mAppIcon == null) {
            int i = (int) this.mBatteryHistEntry.mUid;
            String[] packagesForUid = packageManager.getPackagesForUid(i);
            if (packagesForUid == null || packagesForUid.length == 0) {
                BatteryEntry.NameAndIcon nameAndIconFromUid = BatteryEntry.getNameAndIconFromUid(this.mContext, this.mAppLabel, i);
                this.mAppLabel = nameAndIconFromUid.name;
                this.mAppIcon = nameAndIconFromUid.icon;
            }
            BatteryEntry.NameAndIcon loadNameAndIcon = BatteryEntry.loadNameAndIcon(this.mContext, i, (Handler) null, (BatteryEntry) null, packageName, this.mAppLabel, this.mAppIcon);
            BatteryEntry.clearUidCache();
            if (loadNameAndIcon != null) {
                this.mAppLabel = loadNameAndIcon.name;
                this.mAppIcon = loadNameAndIcon.icon;
                String str = loadNameAndIcon.packageName;
                this.mDefaultPackageName = str;
                if (str != null && !str.equals(str)) {
                    Log.w("BatteryDiffEntry", String.format("found different package: %s | %s", new Object[]{this.mDefaultPackageName, loadNameAndIcon.packageName}));
                }
            }
        }
    }

    public String toString() {
        return "BatteryDiffEntry{" + String.format("\n\tname=%s restrictable=%b", new Object[]{this.mAppLabel, Boolean.valueOf(this.mValidForRestriction)}) + String.format("\n\tconsume=%.2f%% %f/%f", new Object[]{Double.valueOf(this.mPercentOfTotal), Double.valueOf(this.mConsumePower), Double.valueOf(this.mTotalConsumePower)}) + String.format("\n\tforeground:%s background:%s", new Object[]{StringUtil.formatElapsedTime(this.mContext, (double) this.mForegroundUsageTimeInMs, true, false), StringUtil.formatElapsedTime(this.mContext, (double) this.mBackgroundUsageTimeInMs, true, false)}) + String.format("\n\tpackage:%s|%s uid:%d userId:%d", new Object[]{this.mBatteryHistEntry.mPackageName, getPackageName(), Long.valueOf(this.mBatteryHistEntry.mUid), Long.valueOf(this.mBatteryHistEntry.mUserId)});
    }

    public static void clearCache() {
        sResourceCache.clear();
        sValidForRestriction.clear();
    }

    private Drawable getBadgeIconForUser(Drawable drawable) {
        int userId = UserHandle.getUserId((int) this.mBatteryHistEntry.mUid);
        return userId == 0 ? drawable : this.mUserManager.getBadgedIconForUser(drawable, new UserHandle(userId));
    }

    private static boolean isSystemUid(int i) {
        int appId = UserHandle.getAppId(i);
        return appId >= 1000 && appId < 10000;
    }
}
