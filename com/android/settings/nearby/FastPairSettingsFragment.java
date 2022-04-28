package com.android.settings.nearby;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.widget.MainSwitchPreference;
import java.util.Objects;

public class FastPairSettingsFragment extends SettingsPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.fast_pair_settings);

    public int getHelpResource() {
        return 0;
    }

    public int getMetricsCategory() {
        return 1910;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.fast_pair_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) findPreference("fast_pair_scan_switch");
        Objects.requireNonNull(mainSwitchPreference);
        MainSwitchPreference mainSwitchPreference2 = mainSwitchPreference;
        mainSwitchPreference.addOnSwitchChangeListener(new FastPairSettingsFragment$$ExternalSyntheticLambda1(this));
        mainSwitchPreference.setChecked(isFastPairScanAvailable());
        Preference findPreference = findPreference("saved_devices");
        Objects.requireNonNull(findPreference);
        Preference preference = findPreference;
        findPreference.setOnPreferenceClickListener(new FastPairSettingsFragment$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(Switch switchR, boolean z) {
        Settings.Secure.putInt(getContentResolver(), "fast_pair_scan_enabled", z ? 1 : 0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$1(Preference preference) {
        Intent savedDevicesIntent = getSavedDevicesIntent();
        if (savedDevicesIntent == null || getActivity() == null) {
            return true;
        }
        getActivity().startActivity(savedDevicesIntent);
        return true;
    }

    private boolean isFastPairScanAvailable() {
        return Settings.Secure.getInt(getContentResolver(), "fast_pair_scan_enabled", 1) != 0;
    }

    private ComponentName getSavedDevicesComponent() {
        String string = Settings.Secure.getString(getContentResolver(), "nearby_fast_pair_settings_devices_component");
        if (TextUtils.isEmpty(string)) {
            string = getString(17039917);
        }
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return ComponentName.unflattenFromString(string);
    }

    private Intent getSavedDevicesIntent() {
        ComponentName savedDevicesComponent = getSavedDevicesComponent();
        if (savedDevicesComponent == null) {
            return null;
        }
        PackageManager packageManager = getPackageManager();
        Intent intent = getIntent();
        intent.setAction("android.intent.action.VIEW");
        intent.setComponent(savedDevicesComponent);
        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 128);
        if (resolveActivity != null && resolveActivity.activityInfo != null) {
            return intent;
        }
        Log.e("FastPairSettingsFrag", "Device-specified fast pair component (" + savedDevicesComponent + ") not available");
        return null;
    }
}
