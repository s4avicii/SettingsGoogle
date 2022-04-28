package com.google.android.settings.games;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class GameSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.game_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            PackageManager packageManager = context.getPackageManager();
            return Build.IS_DEBUGGABLE || (packageManager != null && packageManager.hasSystemFeature("com.google.android.feature.GAME_OVERLAY"));
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "GameSettings";
    }

    public int getMetricsCategory() {
        return 1886;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.game_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((GameDashboardAlwaysOnController) use(GameDashboardAlwaysOnController.class)).init(getLifecycle());
        ((GameDashboardDNDController) use(GameDashboardDNDController.class)).init(getLifecycle());
    }
}
