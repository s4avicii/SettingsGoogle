package com.android.settings.wifi.dpp;

import android.content.Intent;
import android.util.EventLog;
import android.util.Log;
import androidx.fragment.app.FragmentTransaction;
import androidx.window.C0444R;
import com.android.settings.wifi.dpp.WifiDppQrCodeScannerFragment;
import com.android.settingslib.wifi.WifiRestrictionsCache;

public class WifiDppEnrolleeActivity extends WifiDppBaseActivity implements WifiDppQrCodeScannerFragment.OnScanWifiDppSuccessListener {
    protected WifiRestrictionsCache mWifiRestrictionsCache;

    public int getMetricsCategory() {
        return 1596;
    }

    public void onScanWifiDppSuccess(WifiQrCode wifiQrCode) {
    }

    /* access modifiers changed from: protected */
    public void handleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) {
            finish();
        } else if (!isWifiConfigAllowed()) {
            Log.e("WifiDppEnrolleeActivity", "The user is not allowed to configure Wi-Fi.");
            finish();
            EventLog.writeEvent(1397638484, new Object[]{"202017876", Integer.valueOf(getApplicationContext().getUserId()), "The user is not allowed to configure Wi-Fi."});
        } else if (!action.equals("android.settings.WIFI_DPP_ENROLLEE_QR_CODE_SCANNER")) {
            Log.e("WifiDppEnrolleeActivity", "Launch with an invalid action");
            finish();
        } else {
            showQrCodeScannerFragment(intent.getStringExtra("ssid"));
        }
    }

    private boolean isWifiConfigAllowed() {
        if (this.mWifiRestrictionsCache == null) {
            this.mWifiRestrictionsCache = WifiRestrictionsCache.getInstance(getApplicationContext());
        }
        return this.mWifiRestrictionsCache.isConfigWifiAllowed().booleanValue();
    }

    /* access modifiers changed from: protected */
    public void showQrCodeScannerFragment(String str) {
        WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment = (WifiDppQrCodeScannerFragment) this.mFragmentManager.findFragmentByTag("qr_code_scanner_fragment");
        if (wifiDppQrCodeScannerFragment == null) {
            WifiDppQrCodeScannerFragment wifiDppQrCodeScannerFragment2 = new WifiDppQrCodeScannerFragment(str);
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(C0444R.C0448id.fragment_container, wifiDppQrCodeScannerFragment2, "qr_code_scanner_fragment");
            beginTransaction.commit();
        } else if (!wifiDppQrCodeScannerFragment.isVisible()) {
            this.mFragmentManager.popBackStackImmediate();
        }
    }
}
