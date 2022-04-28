package com.android.settings.development;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import androidx.window.C0444R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class DesktopModePreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin, RebootConfirmationDialogHost {
    static final int SETTING_VALUE_OFF = 0;
    static final int SETTING_VALUE_ON = 1;
    private final DevelopmentSettingsDashboardFragment mFragment;

    public String getPreferenceKey() {
        return "force_desktop_mode_on_external_displays";
    }

    public DesktopModePreferenceController(Context context, DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        super(context);
        this.mFragment = developmentSettingsDashboardFragment;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        Settings.Global.putInt(this.mContext.getContentResolver(), "force_desktop_mode_on_external_displays", booleanValue ? 1 : 0);
        if (!booleanValue) {
            return true;
        }
        RebootConfirmationDialogFragment.show(this.mFragment, C0444R.string.reboot_dialog_force_desktop_mode, this);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = false;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "force_desktop_mode_on_external_displays", 0);
        SwitchPreference switchPreference = (SwitchPreference) this.mPreference;
        if (i != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        Settings.Global.putInt(this.mContext.getContentResolver(), "force_desktop_mode_on_external_displays", 0);
        ((SwitchPreference) this.mPreference).setChecked(false);
    }

    public void onRebootConfirmed() {
        this.mContext.startActivity(new Intent("android.intent.action.REBOOT"));
    }

    /* access modifiers changed from: package-private */
    public String getBuildType() {
        return Build.TYPE;
    }
}
