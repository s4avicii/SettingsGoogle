package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class RebootWithMtePreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin {
    private final DevelopmentSettingsDashboardFragment mFragment;

    public String getPreferenceKey() {
        return "reboot_with_mte";
    }

    public RebootWithMtePreferenceController(Context context, DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        super(context);
        this.mFragment = developmentSettingsDashboardFragment;
    }

    public boolean isAvailable() {
        return SystemProperties.getBoolean("ro.arm64.memtag.bootctl_supported", false);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (Utils.isMonkeyRunning() || !TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        RebootWithMteDialog.show(this.mContext, this.mFragment);
        return true;
    }
}
