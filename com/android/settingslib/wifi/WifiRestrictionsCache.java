package com.android.settingslib.wifi;

import android.content.Context;
import android.os.Bundle;
import android.os.UserManager;
import android.util.SparseArray;
import java.util.HashMap;
import java.util.Map;

public class WifiRestrictionsCache {
    protected static final SparseArray<WifiRestrictionsCache> sInstances = new SparseArray<>();
    protected final Map<String, Boolean> mRestrictions = new HashMap();
    protected UserManager mUserManager;
    protected Bundle mUserRestrictions;

    public static WifiRestrictionsCache getInstance(Context context) {
        int userId = context.getUserId();
        SparseArray<WifiRestrictionsCache> sparseArray = sInstances;
        synchronized (sparseArray) {
            if (sparseArray.indexOfKey(userId) >= 0) {
                WifiRestrictionsCache wifiRestrictionsCache = sparseArray.get(userId);
                return wifiRestrictionsCache;
            }
            WifiRestrictionsCache wifiRestrictionsCache2 = new WifiRestrictionsCache(context);
            sparseArray.put(context.getUserId(), wifiRestrictionsCache2);
            return wifiRestrictionsCache2;
        }
    }

    protected WifiRestrictionsCache(Context context) {
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        this.mUserManager = userManager;
        if (userManager != null) {
            this.mUserRestrictions = userManager.getUserRestrictions();
        }
    }

    public Boolean getRestriction(String str) {
        if (this.mUserRestrictions == null) {
            return Boolean.FALSE;
        }
        synchronized (this.mRestrictions) {
            if (this.mRestrictions.containsKey(str)) {
                Boolean bool = this.mRestrictions.get(str);
                return bool;
            }
            Boolean valueOf = Boolean.valueOf(this.mUserRestrictions.getBoolean(str));
            this.mRestrictions.put(str, valueOf);
            return valueOf;
        }
    }

    public Boolean isConfigWifiAllowed() {
        return Boolean.valueOf(!getRestriction("no_config_wifi").booleanValue());
    }
}
