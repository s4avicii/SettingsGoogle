package com.android.settings.wallpaper;

import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;

public class WallpaperTypeSettings extends DashboardFragment {
    public int getHelpResource() {
        return C0444R.string.help_uri_wallpaper;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "WallpaperTypeSettings";
    }

    public int getMetricsCategory() {
        return 101;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.wallpaper_settings;
    }
}
