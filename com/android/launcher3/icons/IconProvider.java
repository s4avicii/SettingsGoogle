package com.android.launcher3.icons;

import android.content.res.Resources;
import androidx.core.p002os.BuildCompat;
import com.android.settingslib.applications.RecentAppOpsAccess;

public class IconProvider {
    public static final boolean ATLEAST_T = BuildCompat.isAtLeastT();
    private static final int CONFIG_ICON_MASK_RES_ID = Resources.getSystem().getIdentifier("config_icon_mask", "string", RecentAppOpsAccess.ANDROID_SYSTEM_PACKAGE_NAME);
}
