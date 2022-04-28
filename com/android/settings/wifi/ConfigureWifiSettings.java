package com.android.settings.wifi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.wifi.p2p.WifiP2pPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ConfigureWifiSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.wifi_configure_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return context.getResources().getBoolean(C0444R.bool.config_show_wifi_settings);
        }
    };
    private Preference mCertinstallerPreference;
    private WifiWakeupPreferenceController mWifiWakeupPreferenceController;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ConfigureWifiSettings";
    }

    public int getMetricsCategory() {
        return 338;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.wifi_configure_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivity().setTitle(C0444R.string.network_and_internet_preferences_title);
        Preference findPreference = findPreference("install_credentials");
        this.mCertinstallerPreference = findPreference;
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(new ConfigureWifiSettings$$ExternalSyntheticLambda0(this));
        } else {
            Log.d("ConfigureWifiSettings", "Can not find the preference.");
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$0(Preference preference) {
        Intent intent = new Intent("android.credentials.INSTALL");
        intent.setFlags(268435456);
        intent.setComponent(new ComponentName("com.android.certinstaller", "com.android.certinstaller.CertInstallerMain"));
        intent.putExtra("certificate_install_usage", "wifi");
        getContext().startActivity(intent);
        return true;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WifiP2pPreferenceController(context, getSettingsLifecycle(), (WifiManager) getSystemService("wifi")));
        return arrayList;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        WifiWakeupPreferenceController wifiWakeupPreferenceController = (WifiWakeupPreferenceController) use(WifiWakeupPreferenceController.class);
        this.mWifiWakeupPreferenceController = wifiWakeupPreferenceController;
        wifiWakeupPreferenceController.setFragment(this);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 600) {
            this.mWifiWakeupPreferenceController.onActivityResult(i, i2);
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }
}
