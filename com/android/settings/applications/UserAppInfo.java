package com.android.settings.applications;

import android.content.pm.ApplicationInfo;
import android.content.pm.UserInfo;
import android.text.TextUtils;
import java.util.Objects;

public class UserAppInfo {
    public final ApplicationInfo appInfo;
    public final UserInfo userInfo;

    public UserAppInfo(UserInfo userInfo2, ApplicationInfo applicationInfo) {
        this.userInfo = userInfo2;
        this.appInfo = applicationInfo;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserAppInfo userAppInfo = (UserAppInfo) obj;
        if (userAppInfo.userInfo.id != this.userInfo.id || !TextUtils.equals(userAppInfo.appInfo.packageName, this.appInfo.packageName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.userInfo.id), this.appInfo.packageName});
    }
}
