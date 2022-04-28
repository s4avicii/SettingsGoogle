package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class NfcStackDebugLogPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final String NFC_STACK_DEBUGLOG_ENABLED_PROPERTY = "persist.nfc.debug_enabled";

    public String getPreferenceKey() {
        return "nfc_stack_debuglog_enabled";
    }

    public NfcStackDebugLogPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        try {
            SystemProperties.set(NFC_STACK_DEBUGLOG_ENABLED_PROPERTY, ((Boolean) obj).booleanValue() ? "true" : "false");
            return true;
        } catch (RuntimeException e) {
            Log.e("PrefControllerMixin", "Fail to set nfc system property: " + e.getMessage());
            return true;
        }
    }

    public void updateState(Preference preference) {
        try {
            ((SwitchPreference) this.mPreference).setChecked(SystemProperties.getBoolean(NFC_STACK_DEBUGLOG_ENABLED_PROPERTY, false));
        } catch (RuntimeException e) {
            Log.e("PrefControllerMixin", "Fail to get nfc system property: " + e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        try {
            SystemProperties.set(NFC_STACK_DEBUGLOG_ENABLED_PROPERTY, "false");
            ((SwitchPreference) this.mPreference).setChecked(false);
        } catch (RuntimeException e) {
            Log.e("PrefControllerMixin", "Fail to set nfc system property: " + e.getMessage());
        }
    }
}
