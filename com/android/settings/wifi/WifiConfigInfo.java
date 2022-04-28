package com.android.settings.wifi;

import android.app.Activity;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.window.C0444R;
import java.util.List;

public class WifiConfigInfo extends Activity {
    private TextView mConfigList;
    private WifiManager mWifiManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        setContentView(C0444R.C0450layout.wifi_config_info);
        this.mConfigList = (TextView) findViewById(C0444R.C0448id.config_list);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mWifiManager.isWifiEnabled()) {
            List<WifiConfiguration> configuredNetworks = this.mWifiManager.getConfiguredNetworks();
            StringBuffer stringBuffer = new StringBuffer();
            for (int size = configuredNetworks.size() - 1; size >= 0; size--) {
                stringBuffer.append(configuredNetworks.get(size));
            }
            this.mConfigList.setText(stringBuffer);
            return;
        }
        this.mConfigList.setText(C0444R.string.wifi_state_disabled);
    }
}