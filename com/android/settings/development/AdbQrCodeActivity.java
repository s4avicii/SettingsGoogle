package com.android.settings.development;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.window.C0444R;
import com.android.settings.wifi.dpp.WifiDppBaseActivity;

public class AdbQrCodeActivity extends WifiDppBaseActivity {
    public int getMetricsCategory() {
        return 1831;
    }

    /* access modifiers changed from: protected */
    public void handleIntent(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AdbQrcodeScannerFragment adbQrcodeScannerFragment = (AdbQrcodeScannerFragment) this.mFragmentManager.findFragmentByTag("adb_qr_code_scanner_fragment");
        if (adbQrcodeScannerFragment == null) {
            AdbQrcodeScannerFragment adbQrcodeScannerFragment2 = new AdbQrcodeScannerFragment();
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(C0444R.C0448id.fragment_container, adbQrcodeScannerFragment2, "adb_qr_code_scanner_fragment");
            beginTransaction.commit();
        } else if (!adbQrcodeScannerFragment.isVisible()) {
            this.mFragmentManager.popBackStackImmediate();
        }
    }
}
