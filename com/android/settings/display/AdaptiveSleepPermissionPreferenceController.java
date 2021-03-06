package com.android.settings.display;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import androidx.preference.PreferenceScreen;
import androidx.window.C0444R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.widget.BannerMessagePreference;

public class AdaptiveSleepPermissionPreferenceController {
    private final Context mContext;
    private final PackageManager mPackageManager;
    @VisibleForTesting
    BannerMessagePreference mPreference;

    public AdaptiveSleepPermissionPreferenceController(Context context) {
        this.mPackageManager = context.getPackageManager();
        this.mContext = context;
    }

    public void addToScreen(PreferenceScreen preferenceScreen) {
        initializePreference();
        if (!AdaptiveSleepPreferenceController.hasSufficientPermission(this.mPackageManager)) {
            preferenceScreen.addPreference(this.mPreference);
        }
    }

    public void updateVisibility() {
        initializePreference();
        this.mPreference.setVisible(!AdaptiveSleepPreferenceController.hasSufficientPermission(this.mPackageManager));
    }

    private void initializePreference() {
        if (this.mPreference == null) {
            String attentionServicePackageName = this.mContext.getPackageManager().getAttentionServicePackageName();
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + attentionServicePackageName));
            BannerMessagePreference bannerMessagePreference = new BannerMessagePreference(this.mContext);
            this.mPreference = bannerMessagePreference;
            bannerMessagePreference.setTitle((int) C0444R.string.adaptive_sleep_title_no_permission);
            this.mPreference.setSummary((int) C0444R.string.adaptive_sleep_summary_no_permission);
            this.mPreference.setPositiveButtonText((int) C0444R.string.adaptive_sleep_manage_permission_button);
            this.mPreference.setPositiveButtonOnClickListener(new C0872x32d3df96(this, intent));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializePreference$0(Intent intent, View view) {
        this.mContext.startActivity(intent);
    }
}
