package com.android.settings.connecteddevice;

import android.content.Context;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.window.C0444R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class NfcAndPaymentFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(C0444R.xml.nfc_and_payment_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            if (((UserManager) context.getSystemService(UserManager.class)).getUserInfo(UserHandle.myUserId()).isGuest()) {
                return false;
            }
            return context.getPackageManager().hasSystemFeature("android.hardware.nfc");
        }
    };

    public int getHelpResource() {
        return C0444R.string.help_uri_nfc_and_payment_settings;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "NfcAndPaymentFragment";
    }

    public int getMetricsCategory() {
        return 1828;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return C0444R.xml.nfc_and_payment_settings;
    }
}
